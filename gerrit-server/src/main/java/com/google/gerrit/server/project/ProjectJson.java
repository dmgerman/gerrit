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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|Strings
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|server
operator|.
name|config
operator|.
name|AllProjectsName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
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

begin_class
DECL|class|ProjectJson
specifier|public
class|class
name|ProjectJson
block|{
DECL|field|allProjects
specifier|private
specifier|final
name|AllProjectsName
name|allProjects
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectJson (AllProjectsName allProjects)
name|ProjectJson
parameter_list|(
name|AllProjectsName
name|allProjects
parameter_list|)
block|{
name|this
operator|.
name|allProjects
operator|=
name|allProjects
expr_stmt|;
block|}
DECL|method|format (ProjectResource rsrc)
specifier|public
name|ProjectInfo
name|format
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|)
block|{
return|return
name|format
argument_list|(
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|)
return|;
block|}
DECL|method|format (Project p)
specifier|public
name|ProjectInfo
name|format
parameter_list|(
name|Project
name|p
parameter_list|)
block|{
name|ProjectInfo
name|info
init|=
operator|new
name|ProjectInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|name
operator|=
name|p
operator|.
name|getName
argument_list|()
expr_stmt|;
name|Project
operator|.
name|NameKey
name|parentName
init|=
name|p
operator|.
name|getParent
argument_list|(
name|allProjects
argument_list|)
decl_stmt|;
name|info
operator|.
name|parent
operator|=
name|parentName
operator|!=
literal|null
condition|?
name|parentName
operator|.
name|get
argument_list|()
else|:
literal|null
expr_stmt|;
name|info
operator|.
name|description
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|p
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|finish
argument_list|()
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|class|ProjectInfo
specifier|public
specifier|static
class|class
name|ProjectInfo
block|{
DECL|field|kind
specifier|public
specifier|final
name|String
name|kind
init|=
literal|"gerritcodereview#project"
decl_stmt|;
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
DECL|field|parent
specifier|public
name|String
name|parent
decl_stmt|;
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|branches
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|branches
decl_stmt|;
DECL|method|finish ()
name|void
name|finish
parameter_list|()
block|{
name|id
operator|=
name|Url
operator|.
name|encode
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

