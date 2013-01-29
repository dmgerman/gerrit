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
name|AuthException
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
name|DefaultInput
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
name|ResourceConflictException
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
name|change
operator|.
name|GetDraft
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
name|server
operator|.
name|change
operator|.
name|PutDraft
operator|.
name|Input
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
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
DECL|class|PutDraft
class|class
name|PutDraft
implements|implements
name|RestModifyView
argument_list|<
name|DraftResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|static
class|class
name|Input
block|{
DECL|field|kind
name|String
name|kind
decl_stmt|;
DECL|field|id
name|String
name|id
decl_stmt|;
DECL|field|path
name|String
name|path
decl_stmt|;
DECL|field|side
name|Side
name|side
decl_stmt|;
DECL|field|line
name|Integer
name|line
decl_stmt|;
DECL|field|inReplyTo
name|String
name|inReplyTo
decl_stmt|;
DECL|field|updated
name|Timestamp
name|updated
decl_stmt|;
comment|// Accepted but ignored.
annotation|@
name|DefaultInput
DECL|field|message
name|String
name|message
decl_stmt|;
block|}
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
name|Provider
argument_list|<
name|DeleteDraft
argument_list|>
name|delete
decl_stmt|;
annotation|@
name|Inject
DECL|method|PutDraft (Provider<ReviewDb> db, Provider<DeleteDraft> delete)
name|PutDraft
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|DeleteDraft
argument_list|>
name|delete
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
block|}
annotation|@
name|Override
DECL|method|apply (DraftResource rsrc, Input in)
specifier|public
name|Object
name|apply
parameter_list|(
name|DraftResource
name|rsrc
parameter_list|,
name|Input
name|in
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|OrmException
block|{
name|PatchLineComment
name|c
init|=
name|rsrc
operator|.
name|getComment
argument_list|()
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
name|get
argument_list|()
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
name|kind
operator|!=
literal|null
operator|&&
operator|!
literal|"gerritcodereview#comment"
operator|.
name|equals
argument_list|(
name|in
operator|.
name|kind
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"expected kind gerritcodereview#comment"
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
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchComments
argument_list|()
operator|.
name|delete
argument_list|(
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
argument_list|)
expr_stmt|;
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchComments
argument_list|()
operator|.
name|insert
argument_list|(
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
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchComments
argument_list|()
operator|.
name|update
argument_list|(
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
return|return
operator|new
name|GetDraft
operator|.
name|Comment
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|update (PatchLineComment e, Input in)
specifier|private
name|PatchLineComment
name|update
parameter_list|(
name|PatchLineComment
name|e
parameter_list|,
name|Input
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
name|GetDraft
operator|.
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
name|line
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|setLine
argument_list|(
name|in
operator|.
name|line
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
name|e
operator|.
name|updated
argument_list|()
expr_stmt|;
return|return
name|e
return|;
block|}
block|}
end_class

end_unit

