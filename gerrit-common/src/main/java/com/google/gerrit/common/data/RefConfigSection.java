begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_class
DECL|class|RefConfigSection
specifier|public
specifier|abstract
class|class
name|RefConfigSection
block|{
comment|/** Pattern that matches all references in a project. */
DECL|field|ALL
specifier|public
specifier|static
specifier|final
name|String
name|ALL
init|=
literal|"refs/*"
decl_stmt|;
comment|/** Pattern that matches all branches in a project. */
DECL|field|HEADS
specifier|public
specifier|static
specifier|final
name|String
name|HEADS
init|=
literal|"refs/heads/*"
decl_stmt|;
comment|/** Configuration settings for a project {@code refs/meta/config} */
DECL|field|REF_CONFIG
specifier|public
specifier|static
specifier|final
name|String
name|REF_CONFIG
init|=
literal|"refs/meta/config"
decl_stmt|;
comment|/** Prefix that triggers a regular expression pattern. */
DECL|field|REGEX_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|REGEX_PREFIX
init|=
literal|"^"
decl_stmt|;
comment|/** @return true if the name is likely to be a valid reference section name. */
DECL|method|isValid (String name)
specifier|public
specifier|static
name|boolean
name|isValid
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|startsWith
argument_list|(
literal|"refs/"
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"^refs/"
argument_list|)
return|;
block|}
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
DECL|method|RefConfigSection ()
specifier|public
name|RefConfigSection
parameter_list|()
block|{   }
DECL|method|RefConfigSection (String name)
specifier|public
name|RefConfigSection
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
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
block|}
block|}
end_class

end_unit

