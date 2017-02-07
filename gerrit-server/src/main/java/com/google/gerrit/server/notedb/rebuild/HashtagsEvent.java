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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
operator|.
name|ToStringHelper
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
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|Set
import|;
end_import

begin_class
DECL|class|HashtagsEvent
class|class
name|HashtagsEvent
extends|extends
name|Event
block|{
DECL|field|hashtags
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|hashtags
decl_stmt|;
DECL|method|HashtagsEvent ( PatchSet.Id psId, Account.Id who, Timestamp when, Set<String> hashtags, Timestamp changeCreatdOn)
name|HashtagsEvent
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|who
parameter_list|,
name|Timestamp
name|when
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|hashtags
parameter_list|,
name|Timestamp
name|changeCreatdOn
parameter_list|)
block|{
name|super
argument_list|(
name|psId
argument_list|,
name|who
argument_list|,
name|who
argument_list|,
name|when
argument_list|,
name|changeCreatdOn
argument_list|,
comment|// Somewhat confusingly, hashtags do not use the setTag method on
comment|// AbstractChangeUpdate, so pass null as the tag.
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|hashtags
operator|=
name|hashtags
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|uniquePerUpdate ()
name|boolean
name|uniquePerUpdate
parameter_list|()
block|{
comment|// Since these are produced from existing commits in the old NoteDb graph,
comment|// we know that there must be one per commit in the rebuilt graph.
return|return
literal|true
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
name|update
operator|.
name|setHashtags
argument_list|(
name|hashtags
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addToString (ToStringHelper helper)
specifier|protected
name|void
name|addToString
parameter_list|(
name|ToStringHelper
name|helper
parameter_list|)
block|{
name|helper
operator|.
name|add
argument_list|(
literal|"hashtags"
argument_list|,
name|hashtags
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

