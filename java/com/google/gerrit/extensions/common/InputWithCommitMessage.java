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
DECL|package|com.google.gerrit.extensions.common
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
name|common
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/** A generic input with a commit message only. */
end_comment

begin_class
DECL|class|InputWithCommitMessage
specifier|public
class|class
name|InputWithCommitMessage
block|{
DECL|field|commitMessage
annotation|@
name|Nullable
specifier|public
name|String
name|commitMessage
decl_stmt|;
DECL|method|InputWithCommitMessage ()
specifier|public
name|InputWithCommitMessage
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|InputWithCommitMessage (@ullable String commitMessage)
specifier|public
name|InputWithCommitMessage
parameter_list|(
annotation|@
name|Nullable
name|String
name|commitMessage
parameter_list|)
block|{
name|this
operator|.
name|commitMessage
operator|=
name|commitMessage
expr_stmt|;
block|}
block|}
end_class

end_unit

