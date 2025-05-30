# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

add_library( # Specifies the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

# Specifies a path to native header files.
include_directories(src/main/cpp/include/)

find_library( # Defines the name of the path variable that stores the
              # location of the NDK library.
              log-lib

              # Specifies the name of the NDK library that
              # CMake needs to locate.
              log )

## Links your native library against one or more other native libraries.
#target_link_libraries( # Specifies the target library.
#                       native-lib
#
#                       # Links the log library to the target library.
#                       ${log-lib} )

add_library( app-glue
             STATIC
             ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c )

## You need to link static libraries against your shared native library.
#target_link_libraries( native-lib app-glue ${log-lib} )

add_library( imported-lib
             SHARED
             IMPORTED )

add_library(...)
set_target_properties( # Specifies the target library.
                       imported-lib

                       # Specifies the parameter you want to define.
                       PROPERTIES IMPORTED_LOCATION

                       # Provides the path to the library you want to import.
                       imported-lib/src/${ANDROID_ABI}/libimported-lib.so )

include_directories( imported-lib/include/ )


target_link_libraries( native-lib imported-lib app-glue ${log-lib} )