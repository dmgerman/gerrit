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
name|common
operator|.
name|changes
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
name|ChangeUtil
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
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|util
operator|.
name|Collections
import|;
end_import

begin_class
DECL|class|CreateDraft
class|class
name|CreateDraft
implements|implements
name|RestModifyView
argument_list|<
name|RevisionResource
argument_list|,
name|Input
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
annotation|@
name|Inject
DECL|method|CreateDraft (Provider<ReviewDb> db)
name|CreateDraft
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc, Input in)
specifier|public
name|Response
argument_list|<
name|CommentInfo
argument_list|>
name|apply
parameter_list|(
name|RevisionResource
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
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|in
operator|.
name|path
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"path must be non-empty"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
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
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"message must be non-empty"
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
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"line must be> 0"
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
name|getEndLine
argument_list|()
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
name|int
name|line
init|=
name|in
operator|.
name|line
operator|!=
literal|null
condition|?
name|in
operator|.
name|line
else|:
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
name|getEndLine
argument_list|()
else|:
literal|0
decl_stmt|;
name|PatchLineComment
name|c
init|=
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
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|line
argument_list|,
name|rsrc
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|Url
operator|.
name|decode
argument_list|(
name|in
operator|.
name|inReplyTo
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|c
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
name|c
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
name|c
operator|.
name|setRange
argument_list|(
name|in
operator|.
name|range
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
name|c
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|created
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
block|}
end_class

end_unit

