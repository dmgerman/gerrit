begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|annotations
operator|.
name|Nullable
import|;
end_import

begin_class
DECL|class|SubmissionId
specifier|public
class|class
name|SubmissionId
block|{
DECL|field|submissionId
specifier|private
specifier|final
name|String
name|submissionId
decl_stmt|;
DECL|method|SubmissionId (Change.Id changeId, @Nullable String topic)
specifier|public
name|SubmissionId
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
annotation|@
name|Nullable
name|String
name|topic
parameter_list|)
block|{
name|submissionId
operator|=
name|topic
operator|!=
literal|null
condition|?
name|String
operator|.
name|format
argument_list|(
literal|"%s-%s"
argument_list|,
name|changeId
argument_list|,
name|topic
argument_list|)
else|:
name|changeId
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
DECL|method|SubmissionId (Change change)
specifier|public
name|SubmissionId
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
name|this
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|change
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|submissionId
return|;
block|}
block|}
end_class

end_unit

