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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|PatchLineCommentsUtil
operator|.
name|setCommentRevId
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
name|common
operator|.
name|TimeUtil
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
name|api
operator|.
name|changes
operator|.
name|DraftInput
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
name|Side
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
name|BadRequestException
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
name|Response
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
name|RestModifyView
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
name|Patch
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
name|PatchLineComment
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
name|server
operator|.
name|ReviewDb
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
name|PatchLineCommentsUtil
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
name|notedb
operator|.
name|ChangeUpdate
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
name|patch
operator|.
name|PatchListCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PutDraft
class|class
name|PutDraft
implements|implements
name|RestModifyView
argument_list|<
name|DraftResource
argument_list|,
name|DraftInput
argument_list|>
block|{
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|delete
specifier|private
specifier|final
name|DeleteDraft
name|delete
decl_stmt|;
DECL|field|plcUtil
specifier|private
specifier|final
name|PatchLineCommentsUtil
name|plcUtil
decl_stmt|;
DECL|field|updateFactory
specifier|private
specifier|final
name|ChangeUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
DECL|field|patchListCache
specifier|private
specifier|final
name|PatchListCache
name|patchListCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|PutDraft (Provider<ReviewDb> db, DeleteDraft delete, PatchLineCommentsUtil plcUtil, ChangeUpdate.Factory updateFactory, PatchListCache patchListCache)
name|PutDraft
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|DeleteDraft
name|delete
parameter_list|,
name|PatchLineCommentsUtil
name|plcUtil
parameter_list|,
name|ChangeUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|PatchListCache
name|patchListCache
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
name|this
operator|.
name|plcUtil
operator|=
name|plcUtil
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
name|this
operator|.
name|patchListCache
operator|=
name|patchListCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (DraftResource rsrc, DraftInput in)
specifier|public
name|Response
argument_list|<
name|CommentInfo
argument_list|>
name|apply
parameter_list|(
name|DraftResource
name|rsrc
parameter_list|,
name|DraftInput
name|in
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|PatchLineComment
name|c
init|=
name|rsrc
operator|.
name|getComment
argument_list|()
decl_stmt|;
name|ChangeUpdate
name|update
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getControl
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
operator|||
name|in
operator|.
name|message
operator|==
literal|null
operator|||
name|in
operator|.
name|message
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|delete
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|,
literal|null
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|in
operator|.
name|id
operator|!=
literal|null
operator|&&
operator|!
name|rsrc
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|in
operator|.
name|id
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"id must match URL"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|in
operator|.
name|line
operator|!=
literal|null
operator|&&
name|in
operator|.
name|line
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"line must be>= 0"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|in
operator|.
name|line
operator|!=
literal|null
operator|&&
name|in
operator|.
name|range
operator|!=
literal|null
operator|&&
name|in
operator|.
name|line
operator|!=
name|in
operator|.
name|range
operator|.
name|endLine
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"range endLine must be on the same line as the comment"
argument_list|)
throw|;
block|}
if|if
condition|(
name|in
operator|.
name|path
operator|!=
literal|null
operator|&&
operator|!
name|in
operator|.
name|path
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|getFileName
argument_list|()
argument_list|)
condition|)
block|{
comment|// Updating the path alters the primary key, which isn't possible.
comment|// Delete then recreate the comment instead of an update.
name|plcUtil
operator|.
name|deleteComments
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|update
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|=
operator|new
name|PatchLineComment
argument_list|(
operator|new
name|PatchLineComment
operator|.
name|Key
argument_list|(
operator|new
name|Patch
operator|.
name|Key
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|in
operator|.
name|path
argument_list|)
argument_list|,
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|c
operator|.
name|getLine
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getAuthorId
argument_list|()
argument_list|,
name|c
operator|.
name|getParentUuid
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
expr_stmt|;
name|setCommentRevId
argument_list|(
name|c
argument_list|,
name|patchListCache
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|)
expr_stmt|;
name|plcUtil
operator|.
name|insertComments
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|update
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|update
argument_list|(
name|c
argument_list|,
name|in
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|c
operator|.
name|getRevId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setCommentRevId
argument_list|(
name|c
argument_list|,
name|patchListCache
argument_list|,
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|plcUtil
operator|.
name|updateComments
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|update
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|update
argument_list|(
name|c
argument_list|,
name|in
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|update
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
operator|new
name|CommentInfo
argument_list|(
name|c
argument_list|,
literal|null
argument_list|)
argument_list|)
return|;
block|}
DECL|method|update (PatchLineComment e, DraftInput in)
specifier|private
name|PatchLineComment
name|update
parameter_list|(
name|PatchLineComment
name|e
parameter_list|,
name|DraftInput
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|.
name|side
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|setSide
argument_list|(
name|in
operator|.
name|side
operator|==
name|Side
operator|.
name|PARENT
condition|?
operator|(
name|short
operator|)
literal|0
else|:
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|inReplyTo
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|setParentUuid
argument_list|(
name|Url
operator|.
name|decode
argument_list|(
name|in
operator|.
name|inReplyTo
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|setMessage
argument_list|(
name|in
operator|.
name|message
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|in
operator|.
name|range
operator|!=
literal|null
operator|||
name|in
operator|.
name|line
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|fromRange
argument_list|(
name|in
operator|.
name|range
argument_list|)
expr_stmt|;
name|e
operator|.
name|setLine
argument_list|(
name|in
operator|.
name|range
operator|!=
literal|null
condition|?
name|in
operator|.
name|range
operator|.
name|endLine
else|:
name|in
operator|.
name|line
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|setWrittenOn
argument_list|(
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|e
return|;
block|}
block|}
end_class

end_unit

