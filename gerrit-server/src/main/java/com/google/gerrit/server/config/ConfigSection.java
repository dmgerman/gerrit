begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/** Provides access to one section from {@link Config} */
end_comment

begin_class
DECL|class|ConfigSection
specifier|public
class|class
name|ConfigSection
block|{
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|section
specifier|private
specifier|final
name|String
name|section
decl_stmt|;
DECL|method|ConfigSection (Config cfg, String section)
specifier|public
name|ConfigSection
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|section
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
name|this
operator|.
name|section
operator|=
name|section
expr_stmt|;
block|}
DECL|method|optional (String name)
specifier|public
name|String
name|optional
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getString
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|required (String name)
specifier|public
name|String
name|required
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|ConfigUtil
operator|.
name|getRequired
argument_list|(
name|cfg
argument_list|,
name|section
argument_list|,
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

