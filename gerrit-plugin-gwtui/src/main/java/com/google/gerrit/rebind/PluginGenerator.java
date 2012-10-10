begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
end_comment

begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.rebind
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rebind
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|Generator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|GeneratorContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|TreeLogger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|UnableToCompleteException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|typeinfo
operator|.
name|JClassType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|typeinfo
operator|.
name|TypeOracle
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|rebind
operator|.
name|ClassSourceFileComposerFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|rebind
operator|.
name|SourceWriter
import|;
end_import

begin_comment
comment|/**  * Write the top layer in the Gadget bootstrap sandwich and generate a stub  * manifest that will be completed by the linker.  *  * Based on gwt-gadgets GadgetGenerator class  */
end_comment

begin_class
DECL|class|PluginGenerator
specifier|public
class|class
name|PluginGenerator
extends|extends
name|Generator
block|{
annotation|@
name|Override
DECL|method|generate (TreeLogger logger, GeneratorContext context, String typeName)
specifier|public
name|String
name|generate
parameter_list|(
name|TreeLogger
name|logger
parameter_list|,
name|GeneratorContext
name|context
parameter_list|,
name|String
name|typeName
parameter_list|)
throws|throws
name|UnableToCompleteException
block|{
comment|// The TypeOracle knows about all types in the type system
name|TypeOracle
name|typeOracle
init|=
name|context
operator|.
name|getTypeOracle
argument_list|()
decl_stmt|;
comment|// Get a reference to the type that the generator should implement
name|JClassType
name|sourceType
init|=
name|typeOracle
operator|.
name|findType
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
comment|// Ensure that the requested type exists
if|if
condition|(
name|sourceType
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|ERROR
argument_list|,
literal|"Could not find requested typeName"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnableToCompleteException
argument_list|()
throw|;
block|}
comment|// Make sure the Gadget type is correctly defined
name|validateType
argument_list|(
name|logger
argument_list|,
name|sourceType
argument_list|)
expr_stmt|;
comment|// Pick a name for the generated class to not conflict.
name|String
name|generatedSimpleSourceName
init|=
name|sourceType
operator|.
name|getSimpleSourceName
argument_list|()
operator|+
literal|"PluginImpl"
decl_stmt|;
comment|// Begin writing the generated source.
name|ClassSourceFileComposerFactory
name|f
init|=
operator|new
name|ClassSourceFileComposerFactory
argument_list|(
name|sourceType
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|generatedSimpleSourceName
argument_list|)
decl_stmt|;
name|f
operator|.
name|addImport
argument_list|(
name|GWT
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|f
operator|.
name|setSuperclass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
comment|// All source gets written through this Writer
name|PrintWriter
name|out
init|=
name|context
operator|.
name|tryCreate
argument_list|(
name|logger
argument_list|,
name|sourceType
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|generatedSimpleSourceName
argument_list|)
decl_stmt|;
comment|// If an implementation already exists, we don't need to do any work
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
comment|// We really use a SourceWriter since it's convenient
name|SourceWriter
name|sw
init|=
name|f
operator|.
name|createSourceWriter
argument_list|(
name|context
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|sw
operator|.
name|println
argument_list|(
literal|"public "
operator|+
name|generatedSimpleSourceName
operator|+
literal|"() {"
argument_list|)
expr_stmt|;
name|sw
operator|.
name|indent
argument_list|()
expr_stmt|;
name|sw
operator|.
name|println
argument_list|(
literal|"onModuleLoad();"
argument_list|)
expr_stmt|;
name|sw
operator|.
name|outdent
argument_list|()
expr_stmt|;
name|sw
operator|.
name|println
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
name|sw
operator|.
name|commit
argument_list|(
name|logger
argument_list|)
expr_stmt|;
block|}
return|return
name|f
operator|.
name|getCreatedClassName
argument_list|()
return|;
block|}
DECL|method|validateType (TreeLogger logger, JClassType type)
specifier|protected
name|void
name|validateType
parameter_list|(
name|TreeLogger
name|logger
parameter_list|,
name|JClassType
name|type
parameter_list|)
throws|throws
name|UnableToCompleteException
block|{
if|if
condition|(
operator|!
name|type
operator|.
name|isDefaultInstantiable
argument_list|()
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|TreeLogger
operator|.
name|ERROR
argument_list|,
literal|"Plugin types must be default instantiable"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnableToCompleteException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

