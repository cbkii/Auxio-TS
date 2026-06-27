package antlr;

import antlr.collections.AST;
import antlr.collections.impl.ASTArray;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;

/* JADX INFO: loaded from: classes3.dex */
public class ASTFactory {
    public String theASTNodeType = null;
    public Class theASTNodeTypeClass = null;
    public Hashtable tokenTypeToASTClassMap = null;

    public ASTFactory() {
    }

    public ASTFactory(Hashtable hashtable) {
        setTokenTypeToASTClassMap(hashtable);
    }

    public void addASTChild(ASTPair aSTPair, AST ast) {
        if (ast != null) {
            AST ast2 = aSTPair.root;
            if (ast2 == null) {
                aSTPair.root = ast;
            } else {
                AST ast3 = aSTPair.child;
                if (ast3 == null) {
                    ast2.setFirstChild(ast);
                } else {
                    ast3.setNextSibling(ast);
                }
            }
            aSTPair.child = ast;
            aSTPair.advanceChildToEnd();
        }
    }

    public AST create() {
        return create(0);
    }

    public AST create(int i) {
        AST astCreate = create(getASTNodeType(i));
        if (astCreate != null) {
            astCreate.initialize(i, "");
        }
        return astCreate;
    }

    public AST create(int i, String str) {
        AST astCreate = create(i);
        if (astCreate != null) {
            astCreate.initialize(i, str);
        }
        return astCreate;
    }

    public AST create(int i, String str, String str2) {
        AST astCreate = create(str2);
        if (astCreate != null) {
            astCreate.initialize(i, str);
        }
        return astCreate;
    }

    public AST create(Token token) {
        AST astCreate = create(token.getType());
        if (astCreate != null) {
            astCreate.initialize(token);
        }
        return astCreate;
    }

    public AST create(Token token, String str) {
        return createUsingCtor(token, str);
    }

    public AST create(AST ast) {
        if (ast == null) {
            return null;
        }
        AST astCreate = create(ast.getType());
        if (astCreate != null) {
            astCreate.initialize(ast);
        }
        return astCreate;
    }

    public AST create(Class cls) {
        try {
            return (AST) cls.newInstance();
        } catch (Exception unused) {
            StringBuilder sbM5a = C0000a.m5a("Can't create AST Node ");
            sbM5a.append(cls.getName());
            error(sbM5a.toString());
            return null;
        }
    }

    public AST create(String str) {
        try {
            return create(Utils.loadClass(str));
        } catch (Exception unused) {
            throw new IllegalArgumentException(C0000a.m1a("Invalid class, ", str));
        }
    }

    public AST createUsingCtor(Token token, String str) {
        try {
            Class clsLoadClass = Utils.loadClass(str);
            try {
                return (AST) clsLoadClass.getConstructor(Token.class).newInstance(token);
            } catch (NoSuchMethodException unused) {
                AST astCreate = create(clsLoadClass);
                if (astCreate == null) {
                    return astCreate;
                }
                astCreate.initialize(token);
                return astCreate;
            }
        } catch (Exception unused2) {
            throw new IllegalArgumentException(C0000a.m1a("Invalid class or can't make instance, ", str));
        }
    }

    public AST dup(AST ast) {
        if (ast == null) {
            return null;
        }
        AST astCreate = create(ast.getClass());
        astCreate.initialize(ast);
        return astCreate;
    }

    public AST dupList(AST ast) {
        AST astDupTree = dupTree(ast);
        AST nextSibling = astDupTree;
        while (ast != null) {
            ast = ast.getNextSibling();
            nextSibling.setNextSibling(dupTree(ast));
            nextSibling = nextSibling.getNextSibling();
        }
        return astDupTree;
    }

    public AST dupTree(AST ast) {
        AST astDup = dup(ast);
        if (ast != null) {
            astDup.setFirstChild(dupList(ast.getFirstChild()));
        }
        return astDup;
    }

    public void error(String str) {
        System.err.println(str);
    }

    public Class getASTNodeType(int i) {
        Class cls;
        Hashtable hashtable = this.tokenTypeToASTClassMap;
        if (hashtable != null && (cls = (Class) hashtable.get(new Integer(i))) != null) {
            return cls;
        }
        Class cls2 = this.theASTNodeTypeClass;
        return cls2 != null ? cls2 : CommonAST.class;
    }

    public Hashtable getTokenTypeToASTClassMap() {
        return this.tokenTypeToASTClassMap;
    }

    public AST make(ASTArray aSTArray) {
        return make(aSTArray.array);
    }

    public AST make(AST[] astArr) {
        AST nextSibling = null;
        if (astArr == null || astArr.length == 0) {
            return null;
        }
        AST ast = astArr[0];
        if (ast != null) {
            ast.setFirstChild(null);
        }
        for (int i = 1; i < astArr.length; i++) {
            if (astArr[i] != null) {
                if (ast == null) {
                    ast = astArr[i];
                    nextSibling = ast;
                } else if (nextSibling == null) {
                    ast.setFirstChild(astArr[i]);
                    nextSibling = ast.getFirstChild();
                } else {
                    nextSibling.setNextSibling(astArr[i]);
                    nextSibling = nextSibling.getNextSibling();
                }
                while (nextSibling.getNextSibling() != null) {
                    nextSibling = nextSibling.getNextSibling();
                }
            }
        }
        return ast;
    }

    public void makeASTRoot(ASTPair aSTPair, AST ast) {
        if (ast != null) {
            ast.addChild(aSTPair.root);
            aSTPair.child = aSTPair.root;
            aSTPair.advanceChildToEnd();
            aSTPair.root = ast;
        }
    }

    public void setASTNodeClass(Class cls) {
        if (cls != null) {
            this.theASTNodeTypeClass = cls;
            this.theASTNodeType = cls.getName();
        }
    }

    public void setASTNodeClass(String str) {
        this.theASTNodeType = str;
        try {
            this.theASTNodeTypeClass = Utils.loadClass(str);
        } catch (Exception unused) {
            error("Can't find/access AST Node type" + str);
        }
    }

    public void setASTNodeType(String str) {
        setASTNodeClass(str);
    }

    public void setTokenTypeASTNodeType(int i, String str) {
        if (this.tokenTypeToASTClassMap == null) {
            this.tokenTypeToASTClassMap = new Hashtable();
        }
        if (str == null) {
            this.tokenTypeToASTClassMap.remove(new Integer(i));
            return;
        }
        try {
            this.tokenTypeToASTClassMap.put(new Integer(i), Utils.loadClass(str));
        } catch (Exception unused) {
            throw new IllegalArgumentException(C0000a.m1a("Invalid class, ", str));
        }
    }

    public void setTokenTypeToASTClassMap(Hashtable hashtable) {
        this.tokenTypeToASTClassMap = hashtable;
    }
}
