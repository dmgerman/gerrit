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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|entities
operator|.
name|Account
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
name|entities
operator|.
name|Comment
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
name|entities
operator|.
name|PatchSet
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
name|RestResource
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
name|RestView
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
name|TypeLiteral
import|;
end_import

begin_class
DECL|class|CommentResource
specifier|public
class|class
name|CommentResource
implements|implements
name|RestResource
block|{
DECL|field|COMMENT_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|CommentResource
argument_list|>
argument_list|>
name|COMMENT_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|CommentResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|rev
specifier|private
specifier|final
name|RevisionResource
name|rev
decl_stmt|;
DECL|field|comment
specifier|private
specifier|final
name|Comment
name|comment
decl_stmt|;
DECL|method|CommentResource (RevisionResource rev, Comment c)
specifier|public
name|CommentResource
parameter_list|(
name|RevisionResource
name|rev
parameter_list|,
name|Comment
name|c
parameter_list|)
block|{
name|this
operator|.
name|rev
operator|=
name|rev
expr_stmt|;
name|this
operator|.
name|comment
operator|=
name|c
expr_stmt|;
block|}
DECL|method|getPatchSet ()
specifier|public
name|PatchSet
name|getPatchSet
parameter_list|()
block|{
return|return
name|rev
operator|.
name|getPatchSet
argument_list|()
return|;
block|}
DECL|method|getComment ()
specifier|public
name|Comment
name|getComment
parameter_list|()
block|{
return|return
name|comment
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|comment
operator|.
name|key
operator|.
name|uuid
return|;
block|}
DECL|method|getAuthorId ()
specifier|public
name|Account
operator|.
name|Id
name|getAuthorId
parameter_list|()
block|{
return|return
name|comment
operator|.
name|author
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|getRevisionResource ()
specifier|public
name|RevisionResource
name|getRevisionResource
parameter_list|()
block|{
return|return
name|rev
return|;
block|}
block|}
end_class

end_unit

