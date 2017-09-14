begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.projects
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|projects
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
name|base
operator|.
name|Joiner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|Url
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
name|List
import|;
end_import

begin_class
DECL|class|DashboardInfo
specifier|public
class|class
name|DashboardInfo
block|{
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|definingProject
specifier|public
name|String
name|definingProject
decl_stmt|;
DECL|field|ref
specifier|public
name|String
name|ref
decl_stmt|;
DECL|field|path
specifier|public
name|String
name|path
decl_stmt|;
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|foreach
specifier|public
name|String
name|foreach
decl_stmt|;
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|isDefault
specifier|public
name|Boolean
name|isDefault
decl_stmt|;
DECL|field|title
specifier|public
name|String
name|title
decl_stmt|;
DECL|field|sections
specifier|public
name|List
argument_list|<
name|DashboardSectionInfo
argument_list|>
name|sections
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|DashboardInfo (String ref, String name)
specifier|public
name|DashboardInfo
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|Joiner
operator|.
name|on
argument_list|(
literal|':'
argument_list|)
operator|.
name|join
argument_list|(
name|Url
operator|.
name|encode
argument_list|(
name|ref
argument_list|)
argument_list|,
name|Url
operator|.
name|encode
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

