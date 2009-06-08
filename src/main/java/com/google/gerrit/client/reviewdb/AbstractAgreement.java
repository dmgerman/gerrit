begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** Base for {@link AccountAgreement} or {@link AccountGroupAgreement}. */
end_comment

begin_interface
DECL|interface|AbstractAgreement
specifier|public
interface|interface
name|AbstractAgreement
block|{
DECL|enum|Status
specifier|public
specifier|static
enum|enum
name|Status
block|{
DECL|enumConstant|NEW
name|NEW
argument_list|(
literal|'n'
argument_list|)
block|,
DECL|enumConstant|VERIFIED
name|VERIFIED
argument_list|(
literal|'V'
argument_list|)
block|,
DECL|enumConstant|REJECTED
name|REJECTED
argument_list|(
literal|'R'
argument_list|)
block|;
DECL|field|code
specifier|private
specifier|final
name|char
name|code
decl_stmt|;
DECL|method|Status (final char c)
specifier|private
name|Status
parameter_list|(
specifier|final
name|char
name|c
parameter_list|)
block|{
name|code
operator|=
name|c
expr_stmt|;
block|}
DECL|method|getCode ()
specifier|public
name|char
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
DECL|method|forCode (final char c)
specifier|public
specifier|static
name|Status
name|forCode
parameter_list|(
specifier|final
name|char
name|c
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Status
name|s
range|:
name|Status
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|code
operator|==
name|c
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
DECL|method|getAgreementId ()
specifier|public
name|ContributorAgreement
operator|.
name|Id
name|getAgreementId
parameter_list|()
function_decl|;
DECL|method|getAcceptedOn ()
specifier|public
name|Timestamp
name|getAcceptedOn
parameter_list|()
function_decl|;
DECL|method|getStatus ()
specifier|public
name|Status
name|getStatus
parameter_list|()
function_decl|;
DECL|method|getReviewedOn ()
specifier|public
name|Timestamp
name|getReviewedOn
parameter_list|()
function_decl|;
DECL|method|getReviewedBy ()
specifier|public
name|Account
operator|.
name|Id
name|getReviewedBy
parameter_list|()
function_decl|;
DECL|method|getReviewComments ()
specifier|public
name|String
name|getReviewComments
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

