begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.changes
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
name|changes
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
name|CommitInfo
import|;
end_import

begin_class
DECL|class|RelatedChangeAndCommitInfo
specifier|public
class|class
name|RelatedChangeAndCommitInfo
block|{
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|changeId
specifier|public
name|String
name|changeId
decl_stmt|;
DECL|field|commit
specifier|public
name|CommitInfo
name|commit
decl_stmt|;
DECL|field|_changeNumber
specifier|public
name|Integer
name|_changeNumber
decl_stmt|;
DECL|field|_revisionNumber
specifier|public
name|Integer
name|_revisionNumber
decl_stmt|;
DECL|field|_currentRevisionNumber
specifier|public
name|Integer
name|_currentRevisionNumber
decl_stmt|;
DECL|field|status
specifier|public
name|String
name|status
decl_stmt|;
DECL|method|RelatedChangeAndCommitInfo ()
specifier|public
name|RelatedChangeAndCommitInfo
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"project"
argument_list|,
name|project
argument_list|)
operator|.
name|add
argument_list|(
literal|"changeId"
argument_list|,
name|changeId
argument_list|)
operator|.
name|add
argument_list|(
literal|"commit"
argument_list|,
name|toString
argument_list|(
name|commit
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
literal|"_changeNumber"
argument_list|,
name|_changeNumber
argument_list|)
operator|.
name|add
argument_list|(
literal|"_revisionNumber"
argument_list|,
name|_revisionNumber
argument_list|)
operator|.
name|add
argument_list|(
literal|"_currentRevisionNumber"
argument_list|,
name|_currentRevisionNumber
argument_list|)
operator|.
name|add
argument_list|(
literal|"status"
argument_list|,
name|status
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|toString (CommitInfo commit)
specifier|private
specifier|static
name|String
name|toString
parameter_list|(
name|CommitInfo
name|commit
parameter_list|)
block|{
return|return
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|commit
argument_list|)
operator|.
name|add
argument_list|(
literal|"commit"
argument_list|,
name|commit
operator|.
name|commit
argument_list|)
operator|.
name|add
argument_list|(
literal|"parent"
argument_list|,
name|commit
operator|.
name|parents
argument_list|)
operator|.
name|add
argument_list|(
literal|"author"
argument_list|,
name|commit
operator|.
name|author
argument_list|)
operator|.
name|add
argument_list|(
literal|"committer"
argument_list|,
name|commit
operator|.
name|committer
argument_list|)
operator|.
name|add
argument_list|(
literal|"subject"
argument_list|,
name|commit
operator|.
name|subject
argument_list|)
operator|.
name|add
argument_list|(
literal|"message"
argument_list|,
name|commit
operator|.
name|message
argument_list|)
operator|.
name|add
argument_list|(
literal|"webLinks"
argument_list|,
name|commit
operator|.
name|webLinks
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

