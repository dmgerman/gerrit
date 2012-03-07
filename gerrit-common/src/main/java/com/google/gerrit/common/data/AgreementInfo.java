begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountAgreement
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
name|AccountGroupAgreement
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
name|ContributorAgreement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|AgreementInfo
specifier|public
class|class
name|AgreementInfo
block|{
DECL|field|userAccepted
specifier|public
name|List
argument_list|<
name|AccountAgreement
argument_list|>
name|userAccepted
decl_stmt|;
DECL|field|groupAccepted
specifier|public
name|List
argument_list|<
name|AccountGroupAgreement
argument_list|>
name|groupAccepted
decl_stmt|;
DECL|field|agreements
specifier|public
name|Map
argument_list|<
name|ContributorAgreement
operator|.
name|Id
argument_list|,
name|ContributorAgreement
argument_list|>
name|agreements
decl_stmt|;
DECL|method|AgreementInfo ()
specifier|public
name|AgreementInfo
parameter_list|()
block|{   }
DECL|method|setUserAccepted (List<AccountAgreement> a)
specifier|public
name|void
name|setUserAccepted
parameter_list|(
name|List
argument_list|<
name|AccountAgreement
argument_list|>
name|a
parameter_list|)
block|{
name|userAccepted
operator|=
name|a
expr_stmt|;
block|}
DECL|method|setGroupAccepted (List<AccountGroupAgreement> a)
specifier|public
name|void
name|setGroupAccepted
parameter_list|(
name|List
argument_list|<
name|AccountGroupAgreement
argument_list|>
name|a
parameter_list|)
block|{
name|groupAccepted
operator|=
name|a
expr_stmt|;
block|}
DECL|method|setAgreements (Map<ContributorAgreement.Id, ContributorAgreement> a)
specifier|public
name|void
name|setAgreements
parameter_list|(
name|Map
argument_list|<
name|ContributorAgreement
operator|.
name|Id
argument_list|,
name|ContributorAgreement
argument_list|>
name|a
parameter_list|)
block|{
name|agreements
operator|=
name|a
expr_stmt|;
block|}
block|}
end_class

end_unit

