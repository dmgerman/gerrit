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
DECL|package|com.google.gerrit.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|mail
package|;
end_package

begin_comment
comment|/** Variables used by emails to hold data */
end_comment

begin_enum
DECL|enum|MailHeader
specifier|public
enum|enum
name|MailHeader
block|{
comment|// Gerrit metadata holders
DECL|enumConstant|ASSIGNEE
name|ASSIGNEE
argument_list|(
literal|"Gerrit-Assignee"
argument_list|)
block|,
DECL|enumConstant|BRANCH
name|BRANCH
argument_list|(
literal|"Gerrit-Branch"
argument_list|)
block|,
DECL|enumConstant|CC
name|CC
argument_list|(
literal|"Gerrit-CC"
argument_list|)
block|,
DECL|enumConstant|COMMENT_IN_REPLY_TO
name|COMMENT_IN_REPLY_TO
argument_list|(
literal|"Comment-In-Reply-To"
argument_list|)
block|,
DECL|enumConstant|COMMENT_DATE
name|COMMENT_DATE
argument_list|(
literal|"Gerrit-Comment-Date"
argument_list|)
block|,
DECL|enumConstant|CHANGE_ID
name|CHANGE_ID
argument_list|(
literal|"Gerrit-Change-Id"
argument_list|)
block|,
DECL|enumConstant|CHANGE_NUMBER
name|CHANGE_NUMBER
argument_list|(
literal|"Gerrit-Change-Number"
argument_list|)
block|,
DECL|enumConstant|CHANGE_URL
name|CHANGE_URL
argument_list|(
literal|"Gerrit-ChangeURL"
argument_list|)
block|,
DECL|enumConstant|COMMIT
name|COMMIT
argument_list|(
literal|"Gerrit-Commit"
argument_list|)
block|,
DECL|enumConstant|HAS_COMMENTS
name|HAS_COMMENTS
argument_list|(
literal|"Gerrit-HasComments"
argument_list|)
block|,
DECL|enumConstant|HAS_LABELS
name|HAS_LABELS
argument_list|(
literal|"Gerrit-Has-Labels"
argument_list|)
block|,
DECL|enumConstant|MESSAGE_TYPE
name|MESSAGE_TYPE
argument_list|(
literal|"Gerrit-MessageType"
argument_list|)
block|,
DECL|enumConstant|OWNER
name|OWNER
argument_list|(
literal|"Gerrit-Owner"
argument_list|)
block|,
DECL|enumConstant|PATCH_SET
name|PATCH_SET
argument_list|(
literal|"Gerrit-PatchSet"
argument_list|)
block|,
DECL|enumConstant|PROJECT
name|PROJECT
argument_list|(
literal|"Gerrit-Project"
argument_list|)
block|,
DECL|enumConstant|REVIEWER
name|REVIEWER
argument_list|(
literal|"Gerrit-Reviewer"
argument_list|)
block|,
comment|// Commonly used Email headers
DECL|enumConstant|AUTO_SUBMITTED
name|AUTO_SUBMITTED
argument_list|(
literal|"Auto-Submitted"
argument_list|)
block|,
DECL|enumConstant|PRECEDENCE
name|PRECEDENCE
argument_list|(
literal|"Precedence"
argument_list|)
block|,
DECL|enumConstant|REFERENCES
name|REFERENCES
argument_list|(
literal|"References"
argument_list|)
block|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|fieldName
specifier|private
specifier|final
name|String
name|fieldName
decl_stmt|;
DECL|method|MailHeader (String name)
name|MailHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|boolean
name|customHeader
init|=
name|name
operator|.
name|startsWith
argument_list|(
literal|"Gerrit-"
argument_list|)
decl_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
if|if
condition|(
name|customHeader
condition|)
block|{
name|this
operator|.
name|fieldName
operator|=
literal|"X-"
operator|+
name|name
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|fieldName
operator|=
name|name
expr_stmt|;
block|}
block|}
DECL|method|fieldWithDelimiter ()
specifier|public
name|String
name|fieldWithDelimiter
parameter_list|()
block|{
return|return
name|fieldName
argument_list|()
operator|+
literal|": "
return|;
block|}
DECL|method|withDelimiter ()
specifier|public
name|String
name|withDelimiter
parameter_list|()
block|{
return|return
name|name
operator|+
literal|": "
return|;
block|}
DECL|method|fieldName ()
specifier|public
name|String
name|fieldName
parameter_list|()
block|{
return|return
name|fieldName
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_enum

end_unit

