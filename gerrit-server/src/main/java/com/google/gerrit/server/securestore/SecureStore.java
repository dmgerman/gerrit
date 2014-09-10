begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.securestore
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|securestore
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|SecureStore
specifier|public
specifier|abstract
class|class
name|SecureStore
block|{
DECL|class|EntryKey
specifier|public
specifier|static
class|class
name|EntryKey
block|{
DECL|field|name
specifier|public
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|section
specifier|public
specifier|final
name|String
name|section
decl_stmt|;
DECL|field|subsection
specifier|public
specifier|final
name|String
name|subsection
decl_stmt|;
DECL|method|EntryKey (String section, String subsection, String name)
specifier|public
name|EntryKey
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|section
operator|=
name|section
expr_stmt|;
name|this
operator|.
name|subsection
operator|=
name|subsection
expr_stmt|;
block|}
block|}
DECL|method|get (String section, String subsection, String name)
specifier|public
specifier|final
name|String
name|get
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|values
init|=
name|getList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|values
index|[
literal|0
index|]
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getList (String section, String subsection, String name)
specifier|public
specifier|abstract
name|String
index|[]
name|getList
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|set (String section, String subsection, String name, String value)
specifier|public
specifier|final
name|void
name|set
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|setList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setList (String section, String subsection, String name, List<String> values)
specifier|public
specifier|abstract
name|void
name|setList
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
function_decl|;
DECL|method|unset (String section, String subsection, String name)
specifier|public
specifier|abstract
name|void
name|unset
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|list ()
specifier|public
specifier|abstract
name|Iterable
argument_list|<
name|EntryKey
argument_list|>
name|list
parameter_list|()
function_decl|;
block|}
end_class

end_unit

