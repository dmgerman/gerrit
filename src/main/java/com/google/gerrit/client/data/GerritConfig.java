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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|reviewdb
operator|.
name|AuthType
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
name|client
operator|.
name|reviewdb
operator|.
name|Project
import|;
end_import

begin_class
DECL|class|GerritConfig
specifier|public
class|class
name|GerritConfig
implements|implements
name|Cloneable
block|{
DECL|field|canonicalUrl
specifier|protected
name|String
name|canonicalUrl
decl_stmt|;
DECL|field|gitweb
specifier|protected
name|GitwebLink
name|gitweb
decl_stmt|;
DECL|field|useContributorAgreements
specifier|protected
name|boolean
name|useContributorAgreements
decl_stmt|;
DECL|field|useContactInfo
specifier|protected
name|boolean
name|useContactInfo
decl_stmt|;
DECL|field|allowRegisterNewEmail
specifier|protected
name|boolean
name|allowRegisterNewEmail
decl_stmt|;
DECL|field|authType
specifier|protected
name|AuthType
name|authType
decl_stmt|;
DECL|field|useRepoDownload
specifier|protected
name|boolean
name|useRepoDownload
decl_stmt|;
DECL|field|gitDaemonUrl
specifier|protected
name|String
name|gitDaemonUrl
decl_stmt|;
DECL|field|sshdAddress
specifier|protected
name|String
name|sshdAddress
decl_stmt|;
DECL|field|wildProject
specifier|protected
name|Project
operator|.
name|NameKey
name|wildProject
decl_stmt|;
DECL|field|approvalTypes
specifier|protected
name|ApprovalTypes
name|approvalTypes
decl_stmt|;
DECL|method|getCanonicalUrl ()
specifier|public
name|String
name|getCanonicalUrl
parameter_list|()
block|{
return|return
name|canonicalUrl
return|;
block|}
DECL|method|setCanonicalUrl (final String u)
specifier|public
name|void
name|setCanonicalUrl
parameter_list|(
specifier|final
name|String
name|u
parameter_list|)
block|{
name|canonicalUrl
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getAuthType ()
specifier|public
name|AuthType
name|getAuthType
parameter_list|()
block|{
return|return
name|authType
return|;
block|}
DECL|method|setAuthType (final AuthType t)
specifier|public
name|void
name|setAuthType
parameter_list|(
specifier|final
name|AuthType
name|t
parameter_list|)
block|{
name|authType
operator|=
name|t
expr_stmt|;
block|}
DECL|method|getGitwebLink ()
specifier|public
name|GitwebLink
name|getGitwebLink
parameter_list|()
block|{
return|return
name|gitweb
return|;
block|}
DECL|method|setGitwebLink (final GitwebLink w)
specifier|public
name|void
name|setGitwebLink
parameter_list|(
specifier|final
name|GitwebLink
name|w
parameter_list|)
block|{
name|gitweb
operator|=
name|w
expr_stmt|;
block|}
DECL|method|isUseContributorAgreements ()
specifier|public
name|boolean
name|isUseContributorAgreements
parameter_list|()
block|{
return|return
name|useContributorAgreements
return|;
block|}
DECL|method|setUseContributorAgreements (final boolean r)
specifier|public
name|void
name|setUseContributorAgreements
parameter_list|(
specifier|final
name|boolean
name|r
parameter_list|)
block|{
name|useContributorAgreements
operator|=
name|r
expr_stmt|;
block|}
DECL|method|isUseContactInfo ()
specifier|public
name|boolean
name|isUseContactInfo
parameter_list|()
block|{
return|return
name|useContactInfo
return|;
block|}
DECL|method|setUseContactInfo (final boolean r)
specifier|public
name|void
name|setUseContactInfo
parameter_list|(
specifier|final
name|boolean
name|r
parameter_list|)
block|{
name|useContactInfo
operator|=
name|r
expr_stmt|;
block|}
DECL|method|isAllowRegisterNewEmail ()
specifier|public
name|boolean
name|isAllowRegisterNewEmail
parameter_list|()
block|{
return|return
name|allowRegisterNewEmail
return|;
block|}
DECL|method|setAllowRegisterNewEmail (final boolean r)
specifier|public
name|void
name|setAllowRegisterNewEmail
parameter_list|(
specifier|final
name|boolean
name|r
parameter_list|)
block|{
name|allowRegisterNewEmail
operator|=
name|r
expr_stmt|;
block|}
DECL|method|isUseRepoDownload ()
specifier|public
name|boolean
name|isUseRepoDownload
parameter_list|()
block|{
return|return
name|useRepoDownload
return|;
block|}
DECL|method|setUseRepoDownload (final boolean r)
specifier|public
name|void
name|setUseRepoDownload
parameter_list|(
specifier|final
name|boolean
name|r
parameter_list|)
block|{
name|useRepoDownload
operator|=
name|r
expr_stmt|;
block|}
DECL|method|getGitDaemonUrl ()
specifier|public
name|String
name|getGitDaemonUrl
parameter_list|()
block|{
return|return
name|gitDaemonUrl
return|;
block|}
DECL|method|setGitDaemonUrl (String url)
specifier|public
name|void
name|setGitDaemonUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
expr_stmt|;
block|}
name|gitDaemonUrl
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getSshdAddress ()
specifier|public
name|String
name|getSshdAddress
parameter_list|()
block|{
return|return
name|sshdAddress
return|;
block|}
DECL|method|setSshdAddress (final String addr)
specifier|public
name|void
name|setSshdAddress
parameter_list|(
specifier|final
name|String
name|addr
parameter_list|)
block|{
name|sshdAddress
operator|=
name|addr
expr_stmt|;
block|}
DECL|method|getWildProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getWildProject
parameter_list|()
block|{
return|return
name|wildProject
return|;
block|}
DECL|method|setWildProject (final Project.NameKey wp)
specifier|public
name|void
name|setWildProject
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|wp
parameter_list|)
block|{
name|wildProject
operator|=
name|wp
expr_stmt|;
block|}
DECL|method|getApprovalTypes ()
specifier|public
name|ApprovalTypes
name|getApprovalTypes
parameter_list|()
block|{
return|return
name|approvalTypes
return|;
block|}
DECL|method|setApprovalTypes (final ApprovalTypes at)
specifier|public
name|void
name|setApprovalTypes
parameter_list|(
specifier|final
name|ApprovalTypes
name|at
parameter_list|)
block|{
name|approvalTypes
operator|=
name|at
expr_stmt|;
block|}
block|}
end_class

end_unit

