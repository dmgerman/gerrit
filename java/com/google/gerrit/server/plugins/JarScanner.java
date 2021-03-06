begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
operator|.
name|firstNonNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterables
operator|.
name|transform
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ListMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|MultimapBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|IO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|AnnotationVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|FieldVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|MethodVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Opcodes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Type
import|;
end_import

begin_class
DECL|class|JarScanner
specifier|public
class|class
name|JarScanner
implements|implements
name|PluginContentScanner
implements|,
name|AutoCloseable
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|SKIP_ALL
specifier|private
specifier|static
specifier|final
name|int
name|SKIP_ALL
init|=
name|ClassReader
operator|.
name|SKIP_CODE
operator||
name|ClassReader
operator|.
name|SKIP_DEBUG
operator||
name|ClassReader
operator|.
name|SKIP_FRAMES
decl_stmt|;
DECL|field|jarFile
specifier|private
specifier|final
name|JarFile
name|jarFile
decl_stmt|;
DECL|method|JarScanner (Path src)
specifier|public
name|JarScanner
parameter_list|(
name|Path
name|src
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|jarFile
operator|=
operator|new
name|JarFile
argument_list|(
name|src
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|scan ( String pluginName, Iterable<Class<? extends Annotation>> annotations)
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Iterable
argument_list|<
name|ExtensionMetaData
argument_list|>
argument_list|>
name|scan
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|Iterable
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|descriptors
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|JarScanner
operator|.
name|ClassData
argument_list|>
name|rawMap
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|String
argument_list|>
name|classObjToClassDescr
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
name|String
name|descriptor
init|=
name|Type
operator|.
name|getType
argument_list|(
name|annotation
argument_list|)
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|descriptors
operator|.
name|add
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|classObjToClassDescr
operator|.
name|put
argument_list|(
name|annotation
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
block|}
name|Enumeration
argument_list|<
name|JarEntry
argument_list|>
name|e
init|=
name|jarFile
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|JarEntry
name|entry
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|skip
argument_list|(
name|entry
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|ClassData
name|def
init|=
operator|new
name|ClassData
argument_list|(
name|descriptors
argument_list|)
decl_stmt|;
try|try
block|{
operator|new
name|ClassReader
argument_list|(
name|read
argument_list|(
name|jarFile
argument_list|,
name|entry
argument_list|)
argument_list|)
operator|.
name|accept
argument_list|(
name|def
argument_list|,
name|SKIP_ALL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidPluginException
argument_list|(
literal|"Cannot auto-register"
argument_list|,
name|err
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|err
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Plugin %s has invalid class file %s inside of %s"
argument_list|,
name|pluginName
argument_list|,
name|entry
operator|.
name|getName
argument_list|()
argument_list|,
name|jarFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|def
operator|.
name|annotationName
argument_list|)
condition|)
block|{
if|if
condition|(
name|def
operator|.
name|isConcrete
argument_list|()
condition|)
block|{
name|rawMap
operator|.
name|put
argument_list|(
name|def
operator|.
name|annotationName
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Plugin %s tries to @%s(\"%s\") abstract class %s"
argument_list|,
name|pluginName
argument_list|,
name|def
operator|.
name|annotationName
argument_list|,
name|def
operator|.
name|annotationValue
argument_list|,
name|def
operator|.
name|className
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Iterable
argument_list|<
name|ExtensionMetaData
argument_list|>
argument_list|>
name|result
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotoation
range|:
name|annotations
control|)
block|{
name|String
name|descr
init|=
name|classObjToClassDescr
operator|.
name|get
argument_list|(
name|annotoation
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ClassData
argument_list|>
name|discoverdData
init|=
name|rawMap
operator|.
name|get
argument_list|(
name|descr
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ClassData
argument_list|>
name|values
init|=
name|firstNonNull
argument_list|(
name|discoverdData
argument_list|,
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|annotoation
argument_list|,
name|transform
argument_list|(
name|values
argument_list|,
name|cd
lambda|->
operator|new
name|ExtensionMetaData
argument_list|(
name|cd
operator|.
name|className
argument_list|,
name|cd
operator|.
name|annotationValue
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|findSubClassesOf (Class<?> superClass)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findSubClassesOf
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|superClass
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|findSubClassesOf
argument_list|(
name|superClass
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|jarFile
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|findSubClassesOf (String superClass)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|findSubClassesOf
parameter_list|(
name|String
name|superClass
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
name|superClass
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|classes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|JarEntry
argument_list|>
name|e
init|=
name|jarFile
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|JarEntry
name|entry
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|skip
argument_list|(
name|entry
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|ClassData
name|def
init|=
operator|new
name|ClassData
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
operator|new
name|ClassReader
argument_list|(
name|read
argument_list|(
name|jarFile
argument_list|,
name|entry
argument_list|)
argument_list|)
operator|.
name|accept
argument_list|(
name|def
argument_list|,
name|SKIP_ALL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|err
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Jar %s has invalid class file %s"
argument_list|,
name|jarFile
operator|.
name|getName
argument_list|()
argument_list|,
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|def
operator|.
name|superName
argument_list|)
condition|)
block|{
name|classes
operator|.
name|addAll
argument_list|(
name|findSubClassesOf
argument_list|(
name|def
operator|.
name|className
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|def
operator|.
name|isConcrete
argument_list|()
condition|)
block|{
name|classes
operator|.
name|add
argument_list|(
name|def
operator|.
name|className
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|classes
return|;
block|}
DECL|method|skip (JarEntry entry)
specifier|private
specifier|static
name|boolean
name|skip
parameter_list|(
name|JarEntry
name|entry
parameter_list|)
block|{
if|if
condition|(
operator|!
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
comment|// Avoid non-class resources.
block|}
if|if
condition|(
name|entry
operator|.
name|getSize
argument_list|()
operator|<=
literal|0
condition|)
block|{
return|return
literal|true
return|;
comment|// Directories have 0 size.
block|}
if|if
condition|(
name|entry
operator|.
name|getSize
argument_list|()
operator|>=
literal|1024
operator|*
literal|1024
condition|)
block|{
return|return
literal|true
return|;
comment|// Do not scan huge class files.
block|}
return|return
literal|false
return|;
block|}
DECL|method|read (JarFile jarFile, JarEntry entry)
specifier|private
specifier|static
name|byte
index|[]
name|read
parameter_list|(
name|JarFile
name|jarFile
parameter_list|,
name|JarEntry
name|entry
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|entry
operator|.
name|getSize
argument_list|()
index|]
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|jarFile
operator|.
name|getInputStream
argument_list|(
name|entry
argument_list|)
init|)
block|{
name|IO
operator|.
name|readFully
argument_list|(
name|in
argument_list|,
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
DECL|class|ClassData
specifier|public
specifier|static
class|class
name|ClassData
extends|extends
name|ClassVisitor
block|{
DECL|field|access
name|int
name|access
decl_stmt|;
DECL|field|className
name|String
name|className
decl_stmt|;
DECL|field|superName
name|String
name|superName
decl_stmt|;
DECL|field|annotationName
name|String
name|annotationName
decl_stmt|;
DECL|field|annotationValue
name|String
name|annotationValue
decl_stmt|;
DECL|field|interfaces
name|String
index|[]
name|interfaces
decl_stmt|;
DECL|field|exports
name|Collection
argument_list|<
name|String
argument_list|>
name|exports
decl_stmt|;
DECL|method|ClassData (Collection<String> exports)
specifier|private
name|ClassData
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|exports
parameter_list|)
block|{
name|super
argument_list|(
name|Opcodes
operator|.
name|ASM7
argument_list|)
expr_stmt|;
name|this
operator|.
name|exports
operator|=
name|exports
expr_stmt|;
block|}
DECL|method|isConcrete ()
name|boolean
name|isConcrete
parameter_list|()
block|{
return|return
operator|(
name|access
operator|&
name|Opcodes
operator|.
name|ACC_ABSTRACT
operator|)
operator|==
literal|0
operator|&&
operator|(
name|access
operator|&
name|Opcodes
operator|.
name|ACC_INTERFACE
operator|)
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|visit ( int version, int access, String name, String signature, String superName, String[] interfaces)
specifier|public
name|void
name|visit
parameter_list|(
name|int
name|version
parameter_list|,
name|int
name|access
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|signature
parameter_list|,
name|String
name|superName
parameter_list|,
name|String
index|[]
name|interfaces
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|Type
operator|.
name|getObjectType
argument_list|(
name|name
argument_list|)
operator|.
name|getClassName
argument_list|()
expr_stmt|;
name|this
operator|.
name|access
operator|=
name|access
expr_stmt|;
name|this
operator|.
name|superName
operator|=
name|superName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|visitAnnotation (String desc, boolean visible)
specifier|public
name|AnnotationVisitor
name|visitAnnotation
parameter_list|(
name|String
name|desc
parameter_list|,
name|boolean
name|visible
parameter_list|)
block|{
if|if
condition|(
operator|!
name|visible
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Optional
argument_list|<
name|String
argument_list|>
name|found
init|=
name|exports
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|x
lambda|->
name|x
operator|.
name|equals
argument_list|(
name|desc
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
decl_stmt|;
if|if
condition|(
name|found
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|annotationName
operator|=
name|desc
expr_stmt|;
return|return
operator|new
name|AbstractAnnotationVisitor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|visit
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|annotationValue
operator|=
operator|(
name|String
operator|)
name|value
expr_stmt|;
block|}
block|}
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|visitSource (String arg0, String arg1)
specifier|public
name|void
name|visitSource
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|visitOuterClass (String arg0, String arg1, String arg2)
specifier|public
name|void
name|visitOuterClass
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|visitMethod ( int arg0, String arg1, String arg2, String arg3, String[] arg4)
specifier|public
name|MethodVisitor
name|visitMethod
parameter_list|(
name|int
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|,
name|String
name|arg3
parameter_list|,
name|String
index|[]
name|arg4
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|visitInnerClass (String arg0, String arg1, String arg2, int arg3)
specifier|public
name|void
name|visitInnerClass
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|,
name|int
name|arg3
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|visitField (int arg0, String arg1, String arg2, String arg3, Object arg4)
specifier|public
name|FieldVisitor
name|visitField
parameter_list|(
name|int
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|,
name|String
name|arg3
parameter_list|,
name|Object
name|arg4
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|visitEnd ()
specifier|public
name|void
name|visitEnd
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|visitAttribute (Attribute arg0)
specifier|public
name|void
name|visitAttribute
parameter_list|(
name|Attribute
name|arg0
parameter_list|)
block|{}
block|}
DECL|class|AbstractAnnotationVisitor
specifier|private
specifier|abstract
specifier|static
class|class
name|AbstractAnnotationVisitor
extends|extends
name|AnnotationVisitor
block|{
DECL|method|AbstractAnnotationVisitor ()
name|AbstractAnnotationVisitor
parameter_list|()
block|{
name|super
argument_list|(
name|Opcodes
operator|.
name|ASM7
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|visitAnnotation (String arg0, String arg1)
specifier|public
name|AnnotationVisitor
name|visitAnnotation
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|visitArray (String arg0)
specifier|public
name|AnnotationVisitor
name|visitArray
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|visitEnum (String arg0, String arg1, String arg2)
specifier|public
name|void
name|visitEnum
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|visitEnd ()
specifier|public
name|void
name|visitEnd
parameter_list|()
block|{}
block|}
annotation|@
name|Override
DECL|method|getEntry (String resourcePath)
specifier|public
name|Optional
argument_list|<
name|PluginEntry
argument_list|>
name|getEntry
parameter_list|(
name|String
name|resourcePath
parameter_list|)
throws|throws
name|IOException
block|{
name|JarEntry
name|jarEntry
init|=
name|jarFile
operator|.
name|getJarEntry
argument_list|(
name|resourcePath
argument_list|)
decl_stmt|;
if|if
condition|(
name|jarEntry
operator|==
literal|null
operator|||
name|jarEntry
operator|.
name|getSize
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
name|resourceOf
argument_list|(
name|jarEntry
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|entries ()
specifier|public
name|Enumeration
argument_list|<
name|PluginEntry
argument_list|>
name|entries
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|Lists
operator|.
name|transform
argument_list|(
name|Collections
operator|.
name|list
argument_list|(
name|jarFile
operator|.
name|entries
argument_list|()
argument_list|)
argument_list|,
name|jarEntry
lambda|->
block|{
try|try
block|{
return|return
name|resourceOf
argument_list|(
name|jarEntry
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot convert jar entry "
operator|+
name|jarEntry
operator|+
literal|" to a resource"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getInputStream (PluginEntry entry)
specifier|public
name|InputStream
name|getInputStream
parameter_list|(
name|PluginEntry
name|entry
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|jarFile
operator|.
name|getInputStream
argument_list|(
name|jarFile
operator|.
name|getEntry
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getManifest ()
specifier|public
name|Manifest
name|getManifest
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|jarFile
operator|.
name|getManifest
argument_list|()
return|;
block|}
DECL|method|resourceOf (JarEntry jarEntry)
specifier|private
name|PluginEntry
name|resourceOf
parameter_list|(
name|JarEntry
name|jarEntry
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|PluginEntry
argument_list|(
name|jarEntry
operator|.
name|getName
argument_list|()
argument_list|,
name|jarEntry
operator|.
name|getTime
argument_list|()
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|jarEntry
operator|.
name|getSize
argument_list|()
argument_list|)
argument_list|,
name|attributesOf
argument_list|(
name|jarEntry
argument_list|)
argument_list|)
return|;
block|}
DECL|method|attributesOf (JarEntry jarEntry)
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|attributesOf
parameter_list|(
name|JarEntry
name|jarEntry
parameter_list|)
throws|throws
name|IOException
block|{
name|Attributes
name|attributes
init|=
name|jarEntry
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
return|return
name|Maps
operator|.
name|transformEntries
argument_list|(
name|attributes
argument_list|,
parameter_list|(
name|key
parameter_list|,
name|value
parameter_list|)
lambda|->
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

