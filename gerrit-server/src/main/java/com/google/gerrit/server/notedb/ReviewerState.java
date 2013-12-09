begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
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
name|revwalk
operator|.
name|FooterKey
import|;
end_import

begin_comment
comment|/** State of a reviewer on a change. */
end_comment

begin_enum
DECL|enum|ReviewerState
specifier|public
enum|enum
name|ReviewerState
block|{
comment|/** The user has contributed at least one nonzero vote on the change. */
DECL|enumConstant|REVIEWER
name|REVIEWER
argument_list|(
operator|new
name|FooterKey
argument_list|(
literal|"Reviewer"
argument_list|)
argument_list|)
block|,
comment|/** The reviewer was added to the change, but has not voted. */
DECL|enumConstant|CC
name|CC
argument_list|(
operator|new
name|FooterKey
argument_list|(
literal|"CC"
argument_list|)
argument_list|)
block|,
comment|/** The user was previously a reviewer on the change, but was removed. */
DECL|enumConstant|REMOVED
name|REMOVED
argument_list|(
operator|new
name|FooterKey
argument_list|(
literal|"Removed"
argument_list|)
argument_list|)
block|;
DECL|field|footerKey
specifier|private
specifier|final
name|FooterKey
name|footerKey
decl_stmt|;
DECL|method|ReviewerState (FooterKey footerKey)
specifier|private
name|ReviewerState
parameter_list|(
name|FooterKey
name|footerKey
parameter_list|)
block|{
name|this
operator|.
name|footerKey
operator|=
name|footerKey
expr_stmt|;
block|}
DECL|method|getFooterKey ()
name|FooterKey
name|getFooterKey
parameter_list|()
block|{
return|return
name|footerKey
return|;
block|}
block|}
end_enum

end_unit

