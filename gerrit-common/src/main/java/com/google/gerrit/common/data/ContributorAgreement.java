begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/** Portion of a {@link Project} describing a single contributor agreement. */
end_comment

begin_class
DECL|class|ContributorAgreement
specifier|public
class|class
name|ContributorAgreement
implements|implements
name|Comparable
argument_list|<
name|ContributorAgreement
argument_list|>
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|accepted
specifier|private
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|accepted
decl_stmt|;
DECL|field|requireContactInformation
specifier|private
name|boolean
name|requireContactInformation
decl_stmt|;
DECL|field|autoVerify
specifier|private
name|GroupReference
name|autoVerify
decl_stmt|;
DECL|field|agreementUrl
specifier|private
name|String
name|agreementUrl
decl_stmt|;
DECL|method|ContributorAgreement ()
specifier|protected
name|ContributorAgreement
parameter_list|()
block|{   }
DECL|method|ContributorAgreement (String name)
specifier|public
name|ContributorAgreement
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
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
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getAccepted ()
specifier|public
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|getAccepted
parameter_list|()
block|{
if|if
condition|(
name|accepted
operator|==
literal|null
condition|)
block|{
name|accepted
operator|=
operator|new
name|ArrayList
argument_list|<
name|PermissionRule
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|accepted
return|;
block|}
DECL|method|setAccepted (List<PermissionRule> accepted)
specifier|public
name|void
name|setAccepted
parameter_list|(
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|accepted
parameter_list|)
block|{
name|this
operator|.
name|accepted
operator|=
name|accepted
expr_stmt|;
block|}
DECL|method|isRequireContactInformation ()
specifier|public
name|boolean
name|isRequireContactInformation
parameter_list|()
block|{
return|return
name|requireContactInformation
return|;
block|}
DECL|method|setRequireContactInformation (boolean requireContactInformation)
specifier|public
name|void
name|setRequireContactInformation
parameter_list|(
name|boolean
name|requireContactInformation
parameter_list|)
block|{
name|this
operator|.
name|requireContactInformation
operator|=
name|requireContactInformation
expr_stmt|;
block|}
DECL|method|getAutoVerify ()
specifier|public
name|GroupReference
name|getAutoVerify
parameter_list|()
block|{
return|return
name|autoVerify
return|;
block|}
DECL|method|setAutoVerify (GroupReference autoVerify)
specifier|public
name|void
name|setAutoVerify
parameter_list|(
name|GroupReference
name|autoVerify
parameter_list|)
block|{
name|this
operator|.
name|autoVerify
operator|=
name|autoVerify
expr_stmt|;
block|}
DECL|method|getAgreementUrl ()
specifier|public
name|String
name|getAgreementUrl
parameter_list|()
block|{
return|return
name|agreementUrl
return|;
block|}
DECL|method|setAgreementUrl (String agreementUrl)
specifier|public
name|void
name|setAgreementUrl
parameter_list|(
name|String
name|agreementUrl
parameter_list|)
block|{
name|this
operator|.
name|agreementUrl
operator|=
name|agreementUrl
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compareTo (ContributorAgreement o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|ContributorAgreement
name|o
parameter_list|)
block|{
return|return
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o
operator|.
name|getName
argument_list|()
argument_list|)
return|;
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
literal|"ContributorAgreement["
operator|+
name|getName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

