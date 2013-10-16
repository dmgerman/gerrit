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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
package|;
end_package

begin_enum
DECL|enum|GerritTopMenu
specifier|public
enum|enum
name|GerritTopMenu
block|{
DECL|enumConstant|ALL
DECL|enumConstant|MY
DECL|enumConstant|DIFFERENCES
DECL|enumConstant|PROJECTS
DECL|enumConstant|PEOPLE
DECL|enumConstant|PLUGINS
DECL|enumConstant|DOCUMENTATION
name|ALL
block|,
name|MY
block|,
name|DIFFERENCES
block|,
name|PROJECTS
block|,
name|PEOPLE
block|,
name|PLUGINS
block|,
name|DOCUMENTATION
block|;
DECL|field|menuName
specifier|public
specifier|final
name|String
name|menuName
decl_stmt|;
DECL|method|GerritTopMenu ()
specifier|private
name|GerritTopMenu
parameter_list|()
block|{
name|menuName
operator|=
name|name
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|+
name|name
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
block|}
block|}
end_enum

end_unit

