/**
 * Copyright (C) 2013, RedHat, Inc.
 *
 *    http://www.redhat.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef LMDBJNI_H
#define LMDBJNI_H

#ifdef HAVE_CONFIG_H
  /* configure based build.. we will use what it discovered about the platform */
  #include "config.h"
#endif
#if defined(_WIN32) || defined(_WIN64)
    /* Windows based build */
    #define _WIN32_WINNT 0x0501
    #include <windows.h>
#endif
#if !defined(HAVE_CONFIG_H) && (defined(_WIN32) || defined(_WIN64))
    #define HAVE_STDLIB_H 1
    #define HAVE_STRINGS_H 1
#endif

#ifdef HAVE_UNISTD_H
  #include <unistd.h>
#endif

#ifdef HAVE_STDLIB_H
  #include <stdlib.h>
#endif

#ifdef HAVE_STRINGS_H
  #include <string.h>
#endif

#ifdef HAVE_SYS_ERRNO_H
  #include <sys/errno.h>
#endif

#include "hawtjni.h"
#include <stdint.h>
#include <stdarg.h>
#include "lmdb.h"

#ifdef __cplusplus
extern "C" {
#endif

void buffer_copy(const void *source, size_t source_pos, void *dest, size_t dest_pos, size_t length);

#ifdef __cplusplus
} /* extern "C" */
#endif


#endif /* LMDBJNI_H */
