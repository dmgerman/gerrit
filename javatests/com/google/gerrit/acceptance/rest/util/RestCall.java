begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|util
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
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|jdk
operator|.
name|nashorn
operator|.
name|internal
operator|.
name|ir
operator|.
name|annotations
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/** Data container for test REST requests. */
end_comment

begin_class
annotation|@
name|Ignore
annotation|@
name|AutoValue
DECL|class|RestCall
specifier|public
specifier|abstract
class|class
name|RestCall
block|{
DECL|enum|Method
specifier|public
enum|enum
name|Method
block|{
DECL|enumConstant|GET
name|GET
block|,
DECL|enumConstant|PUT
name|PUT
block|,
DECL|enumConstant|POST
name|POST
block|,
DECL|enumConstant|DELETE
name|DELETE
block|}
DECL|method|get (String uriFormat)
specifier|public
specifier|static
name|RestCall
name|get
parameter_list|(
name|String
name|uriFormat
parameter_list|)
block|{
return|return
name|builder
argument_list|(
name|Method
operator|.
name|GET
argument_list|,
name|uriFormat
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|put (String uriFormat)
specifier|public
specifier|static
name|RestCall
name|put
parameter_list|(
name|String
name|uriFormat
parameter_list|)
block|{
return|return
name|builder
argument_list|(
name|Method
operator|.
name|PUT
argument_list|,
name|uriFormat
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|post (String uriFormat)
specifier|public
specifier|static
name|RestCall
name|post
parameter_list|(
name|String
name|uriFormat
parameter_list|)
block|{
return|return
name|builder
argument_list|(
name|Method
operator|.
name|POST
argument_list|,
name|uriFormat
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|delete (String uriFormat)
specifier|public
specifier|static
name|RestCall
name|delete
parameter_list|(
name|String
name|uriFormat
parameter_list|)
block|{
return|return
name|builder
argument_list|(
name|Method
operator|.
name|DELETE
argument_list|,
name|uriFormat
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|builder (Method httpMethod, String uriFormat)
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|Method
name|httpMethod
parameter_list|,
name|String
name|uriFormat
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_RestCall
operator|.
name|Builder
argument_list|()
operator|.
name|httpMethod
argument_list|(
name|httpMethod
argument_list|)
operator|.
name|uriFormat
argument_list|(
name|uriFormat
argument_list|)
return|;
block|}
DECL|method|httpMethod ()
specifier|public
specifier|abstract
name|Method
name|httpMethod
parameter_list|()
function_decl|;
DECL|method|uriFormat ()
specifier|public
specifier|abstract
name|String
name|uriFormat
parameter_list|()
function_decl|;
DECL|method|expectedResponseCode ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Integer
argument_list|>
name|expectedResponseCode
parameter_list|()
function_decl|;
DECL|method|expectedMessage ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|expectedMessage
parameter_list|()
function_decl|;
DECL|method|uri (String... args)
specifier|public
name|String
name|uri
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
block|{
name|String
name|uriFormat
init|=
name|uriFormat
argument_list|()
decl_stmt|;
name|int
name|expectedArgNum
init|=
name|StringUtils
operator|.
name|countMatches
argument_list|(
name|uriFormat
argument_list|,
literal|"%s"
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|args
operator|.
name|length
operator|==
name|expectedArgNum
argument_list|,
literal|"uriFormat %s needs %s arguments, got only %s: %s"
argument_list|,
name|uriFormat
argument_list|,
name|expectedArgNum
argument_list|,
name|args
operator|.
name|length
argument_list|,
name|args
argument_list|)
expr_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
name|uriFormat
argument_list|,
operator|(
name|Object
index|[]
operator|)
name|args
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|httpMethod (Method httpMethod)
specifier|public
specifier|abstract
name|Builder
name|httpMethod
parameter_list|(
name|Method
name|httpMethod
parameter_list|)
function_decl|;
DECL|method|uriFormat (String uriFormat)
specifier|public
specifier|abstract
name|Builder
name|uriFormat
parameter_list|(
name|String
name|uriFormat
parameter_list|)
function_decl|;
DECL|method|expectedResponseCode (int expectedResponseCode)
specifier|public
specifier|abstract
name|Builder
name|expectedResponseCode
parameter_list|(
name|int
name|expectedResponseCode
parameter_list|)
function_decl|;
DECL|method|expectedMessage (String expectedMessage)
specifier|public
specifier|abstract
name|Builder
name|expectedMessage
parameter_list|(
name|String
name|expectedMessage
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|RestCall
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

