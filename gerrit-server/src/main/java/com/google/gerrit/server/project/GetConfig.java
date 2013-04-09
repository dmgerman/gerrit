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
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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
name|git
operator|.
name|GitRepositoryManager
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
DECL|class|GetConfig
specifier|public
class|class
name|GetConfig
implements|implements
name|RestReadView
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|class|ConfigInfo
specifier|public
specifier|static
class|class
name|ConfigInfo
block|{
DECL|field|kind
specifier|public
specifier|final
name|String
name|kind
init|=
literal|"gerritcodereview#project_config"
decl_stmt|;
DECL|field|useContributorAgreements
specifier|public
name|Boolean
name|useContributorAgreements
decl_stmt|;
DECL|field|useContentMerge
specifier|public
name|Boolean
name|useContentMerge
decl_stmt|;
DECL|field|useSignedOffBy
specifier|public
name|Boolean
name|useSignedOffBy
decl_stmt|;
DECL|field|requireChangeId
specifier|public
name|Boolean
name|requireChangeId
decl_stmt|;
DECL|field|commentlinks
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|CommentLinkInfo
argument_list|>
name|commentlinks
decl_stmt|;
DECL|field|theme
specifier|public
name|ThemeInfo
name|theme
decl_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource resource)
specifier|public
name|ConfigInfo
name|apply
parameter_list|(
name|ProjectResource
name|resource
parameter_list|)
block|{
name|ConfigInfo
name|result
init|=
operator|new
name|ConfigInfo
argument_list|()
decl_stmt|;
name|RefControl
name|refConfig
init|=
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|controlForRef
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|)
decl_stmt|;
name|ProjectState
name|project
init|=
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|getProjectState
argument_list|()
decl_stmt|;
if|if
condition|(
name|refConfig
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|result
operator|.
name|useContributorAgreements
operator|=
name|project
operator|.
name|isUseContributorAgreements
argument_list|()
expr_stmt|;
name|result
operator|.
name|useContentMerge
operator|=
name|project
operator|.
name|isUseContentMerge
argument_list|()
expr_stmt|;
name|result
operator|.
name|useSignedOffBy
operator|=
name|project
operator|.
name|isUseSignedOffBy
argument_list|()
expr_stmt|;
name|result
operator|.
name|requireChangeId
operator|=
name|project
operator|.
name|isRequireChangeID
argument_list|()
expr_stmt|;
block|}
comment|// commentlinks are visible to anyone, as they are used for linkification
comment|// on the client side.
name|result
operator|.
name|commentlinks
operator|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
expr_stmt|;
for|for
control|(
name|CommentLinkInfo
name|cl
range|:
name|project
operator|.
name|getCommentLinks
argument_list|()
control|)
block|{
name|result
operator|.
name|commentlinks
operator|.
name|put
argument_list|(
name|cl
operator|.
name|name
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
comment|// Themes are visible to anyone, as they are rendered client-side.
name|result
operator|.
name|theme
operator|=
name|project
operator|.
name|getTheme
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

