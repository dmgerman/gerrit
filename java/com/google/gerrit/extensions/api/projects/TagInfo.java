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
name|gerrit
operator|.
name|extensions
operator|.
name|common
operator|.
name|GitPerson
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
name|common
operator|.
name|WebLinkInfo
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
DECL|class|TagInfo
specifier|public
class|class
name|TagInfo
extends|extends
name|RefInfo
block|{
DECL|field|object
specifier|public
name|String
name|object
decl_stmt|;
DECL|field|message
specifier|public
name|String
name|message
decl_stmt|;
DECL|field|tagger
specifier|public
name|GitPerson
name|tagger
decl_stmt|;
DECL|field|webLinks
specifier|public
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
decl_stmt|;
DECL|method|TagInfo (String ref, String revision, Boolean canDelete, List<WebLinkInfo> webLinks)
specifier|public
name|TagInfo
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|revision
parameter_list|,
name|Boolean
name|canDelete
parameter_list|,
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
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
name|revision
operator|=
name|revision
expr_stmt|;
name|this
operator|.
name|canDelete
operator|=
name|canDelete
expr_stmt|;
name|this
operator|.
name|webLinks
operator|=
name|webLinks
expr_stmt|;
block|}
DECL|method|TagInfo ( String ref, String revision, String object, String message, GitPerson tagger, Boolean canDelete, List<WebLinkInfo> webLinks)
specifier|public
name|TagInfo
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|object
parameter_list|,
name|String
name|message
parameter_list|,
name|GitPerson
name|tagger
parameter_list|,
name|Boolean
name|canDelete
parameter_list|,
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
parameter_list|)
block|{
name|this
argument_list|(
name|ref
argument_list|,
name|revision
argument_list|,
name|canDelete
argument_list|,
name|webLinks
argument_list|)
expr_stmt|;
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|tagger
operator|=
name|tagger
expr_stmt|;
name|this
operator|.
name|webLinks
operator|=
name|webLinks
expr_stmt|;
block|}
block|}
end_class

end_unit

