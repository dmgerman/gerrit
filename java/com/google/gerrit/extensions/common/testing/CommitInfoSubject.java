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
DECL|package|com.google.gerrit.extensions.common.testing
package|package
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
name|testing
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertAbout
import|;
end_import

begin_import
import|import static
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
name|testing
operator|.
name|GitPersonSubject
operator|.
name|gitPersons
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|truth
operator|.
name|ListSubject
operator|.
name|elements
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|FailureMetadata
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|StringSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Subject
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|truth
operator|.
name|ListSubject
import|;
end_import

begin_class
DECL|class|CommitInfoSubject
specifier|public
class|class
name|CommitInfoSubject
extends|extends
name|Subject
block|{
DECL|method|assertThat (CommitInfo commitInfo)
specifier|public
specifier|static
name|CommitInfoSubject
name|assertThat
parameter_list|(
name|CommitInfo
name|commitInfo
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|commits
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|commitInfo
argument_list|)
return|;
block|}
DECL|method|commits ()
specifier|public
specifier|static
name|Subject
operator|.
name|Factory
argument_list|<
name|CommitInfoSubject
argument_list|,
name|CommitInfo
argument_list|>
name|commits
parameter_list|()
block|{
return|return
name|CommitInfoSubject
operator|::
operator|new
return|;
block|}
DECL|field|commitInfo
specifier|private
specifier|final
name|CommitInfo
name|commitInfo
decl_stmt|;
DECL|method|CommitInfoSubject (FailureMetadata failureMetadata, CommitInfo commitInfo)
specifier|private
name|CommitInfoSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|CommitInfo
name|commitInfo
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|commitInfo
argument_list|)
expr_stmt|;
name|this
operator|.
name|commitInfo
operator|=
name|commitInfo
expr_stmt|;
block|}
DECL|method|commit ()
specifier|public
name|StringSubject
name|commit
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"commit"
argument_list|)
operator|.
name|that
argument_list|(
name|commitInfo
operator|.
name|commit
argument_list|)
return|;
block|}
DECL|method|parents ()
specifier|public
name|ListSubject
argument_list|<
name|CommitInfoSubject
argument_list|,
name|CommitInfo
argument_list|>
name|parents
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"parents"
argument_list|)
operator|.
name|about
argument_list|(
name|elements
argument_list|()
argument_list|)
operator|.
name|thatCustom
argument_list|(
name|commitInfo
operator|.
name|parents
argument_list|,
name|commits
argument_list|()
argument_list|)
return|;
block|}
DECL|method|committer ()
specifier|public
name|GitPersonSubject
name|committer
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"committer"
argument_list|)
operator|.
name|about
argument_list|(
name|gitPersons
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|commitInfo
operator|.
name|committer
argument_list|)
return|;
block|}
DECL|method|author ()
specifier|public
name|GitPersonSubject
name|author
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"author"
argument_list|)
operator|.
name|about
argument_list|(
name|gitPersons
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|commitInfo
operator|.
name|author
argument_list|)
return|;
block|}
DECL|method|message ()
specifier|public
name|StringSubject
name|message
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"message"
argument_list|)
operator|.
name|that
argument_list|(
name|commitInfo
operator|.
name|message
argument_list|)
return|;
block|}
block|}
end_class

end_unit

