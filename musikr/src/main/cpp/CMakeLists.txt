# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples of how to use CMake, see https://github.com/android/ndk-samples.

cmake_minimum_required(VERSION 3.22.1)

project("tagJNI") # becomes "libtagJNI.so"

set(taglib_location "${CMAKE_CURRENT_SOURCE_DIR}/taglib")
set(taglib_pkg "${taglib_location}/pkg/${ANDROID_ABI}")
set(taglib_lib "${taglib_pkg}/lib")
set(taglib_include "${taglib_pkg}/include")

set(taglib_file_name libtag.a)
set(taglib_file_path ${taglib_lib}/${taglib_file_name})
add_library(
        "taglib"
        STATIC
        IMPORTED)
set_target_properties(
        "taglib" PROPERTIES
        IMPORTED_LOCATION
        ${taglib_file_path}
        INTERFACE_INCLUDE_DIRECTORIES
        ${taglib_include})

add_library(${CMAKE_PROJECT_NAME} SHARED
        taglib_jni.cpp
        JInputStream.cpp
        JTagMap.cpp
        JMetadataBuilder.cpp
        JClassRef.cpp
        JObjectRef.cpp
        JStringRef.cpp
        JByteArrayRef.cpp
)

# Keep strict warnings scoped to Auxio-owned JNI sources. The imported third-party TagLib archive is
# intentionally not rebuilt with these warning flags.
target_compile_options(${CMAKE_PROJECT_NAME}
        PRIVATE
        -Wall
        -Wextra
        -Wpedantic
        -Werror=return-type)

target_link_options(${CMAKE_PROJECT_NAME}
        # These flags reduce the size of the linked TagLib + JNI shim shared library.
        # https://github.com/taglib/taglib/issues/1212#issuecomment-2326456903
        # Keep 16 KiB maximum page-size compatibility for newer Android devices as well.
        PRIVATE "-Wl,--exclude-libs,ALL,-z,max-page-size=16384")

target_link_libraries(${CMAKE_PROJECT_NAME}
        PRIVATE android
        PRIVATE log
        PRIVATE taglib)
