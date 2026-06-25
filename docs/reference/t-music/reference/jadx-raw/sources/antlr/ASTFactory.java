package antlr;

import antlr.collections.AST;
import antlr.collections.impl.ASTArray;
import java.util.Hashtable;
import p000a.p001a.p002a.p003a.C0000a;

/* loaded from: classes3.dex */
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
        AST create = create(getASTNodeType(i));
        if (create != null) {
            create.initialize(i, "");
        }
        return create;
    }

    public AST create(int i, String str) {
        AST create = create(i);
        if (create != null) {
            create.initialize(i, str);
        }
        return create;
    }

    public AST create(int i, String str, String str2) {
        AST create = create(str2);
        if (create != null) {
            create.initialize(i, str);
        }
        return create;
    }

    public AST create(Token token) {
        AST create = create(token.getType());
        if (create != null) {
            create.initialize(token);
        }
        return create;
    }

    public AST create(Token token, String str) {
        return createUsingCtor(token, str);
    }

    public AST create(AST ast) {
        if (ast == null) {
            return null;
        }
        AST create = create(ast.getType());
        if (create != null) {
            create.initialize(ast);
        }
        return create;
    }

    public AST create(Class cls) {
        try {
            return (AST) cls.newInstance();
        } catch (Exception unused) {
            StringBuilder m5a = C0000a.m5a("Can't create AST Node ");
            m5a.append(cls.getName());
            error(m5a.toString());
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
            Class loadClass = Utils.loadClass(str);
            try {
                return (AST) loadClass.getConstructor(Token.class).newInstance(token);
            } catch (NoSuchMethodException unused) {
                AST create = create(loadClass);
                if (create == null) {
                    return create;
                }
                create.initialize(token);
                return create;
            }
        } catch (Exception unused2) {
            throw new IllegalArgumentException(C0000a.m1a("Invalid class or can't make instance, ", str));
        }
    }

    public AST dup(AST ast) {
        if (ast == null) {
            return null;
        }
        AST create = create(ast.getClass());
        create.initialize(ast);
        return create;
    }

    public AST dupList(AST ast) {
        AST dupTree = dupTree(ast);
        AST ast2 = dupTree;
        while (ast != null) {
            ast = ast.getNextSibling();
            ast2.setNextSibling(dupTree(ast));
            ast2 = ast2.getNextSibling();
        }
        return dupTree;
    }

    public AST dupTree(AST ast) {
        AST dup = dup(ast);
        if (ast != null) {
            dup.setFirstChild(dupList(ast.getFirstChild()));
        }
        return dup;
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
        AST ast = null;
        if (astArr == null || astArr.length == 0) {
            return null;
        }
        AST ast2 = astArr[0];
        if (ast2 != null) {
            ast2.setFirstChild(null);
        }
        for (int i = 1; i < astArr.length; i++) {
            if (astArr[i] != null) {
                if (ast2 == null) {
                    ast2 = astArr[i];
                    ast = ast2;
                } else if (ast == null) {
                    ast2.setFirstChild(astArr[i]);
                    ast = ast2.getFirstChild();
                } else {
                    ast.setNextSibling(astArr[i]);
                    ast = ast.getNextSibling();
                }
                while (ast.getNextSibling() != null) {
                    ast = ast.getNextSibling();
                }
            }
        }
        return ast2;
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
