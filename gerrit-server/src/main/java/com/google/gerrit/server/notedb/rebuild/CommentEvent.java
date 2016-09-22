begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb.rebuild
package|package
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
name|rebuild
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|client
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

begin_class
DECL|class|CommentEvent
class|class
name|CommentEvent
extends|extends
name|Event
block|{
DECL|field|c
specifier|public
specifier|final
name|PatchLineComment
name|c
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|ps
specifier|private
specifier|final
name|PatchSet
name|ps
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|PatchListCache
name|cache
decl_stmt|;
DECL|method|CommentEvent (PatchLineComment c, Change change, PatchSet ps, PatchListCache cache)
name|CommentEvent
parameter_list|(
name|PatchLineComment
name|c
parameter_list|,
name|Change
name|change
parameter_list|,
name|PatchSet
name|ps
parameter_list|,
name|PatchListCache
name|cache
parameter_list|)
block|{
name|super
argument_list|(
name|PatchLineCommentsUtil
operator|.
name|getCommentPsId
argument_list|(
name|c
argument_list|)
argument_list|,
name|c
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|c
operator|.
name|getWrittenOn
argument_list|()
argument_list|,
name|change
operator|.
name|getCreatedOn
argument_list|()
argument_list|,
name|c
operator|.
name|getTag
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|c
operator|=
name|c
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|ps
operator|=
name|ps
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|uniquePerUpdate ()
name|boolean
name|uniquePerUpdate
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeUpdate update)
name|void
name|apply
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|)
throws|throws
name|OrmException
block|{
name|checkUpdate
argument_list|(
name|update
argument_list|)
expr_stmt|;
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
name|cache
argument_list|,
name|change
argument_list|,
name|ps
argument_list|)
expr_stmt|;
block|}
name|update
operator|.
name|putComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

