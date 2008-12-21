begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|IntKey
import|;
end_import

begin_comment
comment|/**  * An agreement {@link Account} must acknowledge to contribute changes.  *   * @see AccountAgreement  */
end_comment

begin_class
DECL|class|ContributorAgreement
specifier|public
specifier|final
class|class
name|ContributorAgreement
block|{
DECL|class|Id
specifier|public
specifier|static
class|class
name|Id
extends|extends
name|IntKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"cla_id"
argument_list|)
DECL|field|id
specifier|protected
name|int
name|id
decl_stmt|;
DECL|method|Id ()
specifier|protected
name|Id
parameter_list|()
block|{     }
DECL|method|Id (final int id)
specifier|public
name|Id
parameter_list|(
specifier|final
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|id
return|;
block|}
block|}
annotation|@
name|Column
DECL|field|id
specifier|protected
name|Id
name|id
decl_stmt|;
comment|/** Is this an active agreement contributors can use. */
annotation|@
name|Column
DECL|field|active
specifier|protected
name|boolean
name|active
decl_stmt|;
comment|/** Is this agreement one that covers a group of people? */
annotation|@
name|Column
DECL|field|groupAgreement
specifier|protected
name|boolean
name|groupAgreement
decl_stmt|;
comment|/** Does this agreement require the {@link Account} to have contact details? */
annotation|@
name|Column
DECL|field|requireContactInformation
specifier|protected
name|boolean
name|requireContactInformation
decl_stmt|;
comment|/** A short name for the agreement. */
annotation|@
name|Column
argument_list|(
name|length
operator|=
literal|40
argument_list|)
DECL|field|shortName
specifier|protected
name|String
name|shortName
decl_stmt|;
comment|/** A short one-line description text to appear next to the name. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|shortDescription
specifier|protected
name|String
name|shortDescription
decl_stmt|;
comment|/** Text of the agreement, in formatted HTML */
annotation|@
name|Column
argument_list|(
name|length
operator|=
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
DECL|field|agreementHtml
specifier|protected
name|String
name|agreementHtml
decl_stmt|;
DECL|method|ContributorAgreement ()
specifier|protected
name|ContributorAgreement
parameter_list|()
block|{   }
comment|/**    * Create a new agreement.    *     * @param newId unique id, see {@link ReviewDb#nextAccountId()}.    * @param name a short title/name for the agreement.    */
DECL|method|ContributorAgreement (final ContributorAgreement.Id newId, final String name)
specifier|public
name|ContributorAgreement
parameter_list|(
specifier|final
name|ContributorAgreement
operator|.
name|Id
name|newId
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
name|id
operator|=
name|newId
expr_stmt|;
name|shortName
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|ContributorAgreement
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|isActive ()
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
name|active
return|;
block|}
DECL|method|setActive (final boolean a)
specifier|public
name|void
name|setActive
parameter_list|(
specifier|final
name|boolean
name|a
parameter_list|)
block|{
name|active
operator|=
name|a
expr_stmt|;
block|}
DECL|method|isGroupAgreement ()
specifier|public
name|boolean
name|isGroupAgreement
parameter_list|()
block|{
return|return
name|groupAgreement
return|;
block|}
DECL|method|setGroupAgreement (final boolean g)
specifier|public
name|void
name|setGroupAgreement
parameter_list|(
specifier|final
name|boolean
name|g
parameter_list|)
block|{
name|groupAgreement
operator|=
name|g
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
DECL|method|setRequireContactInformation (final boolean r)
specifier|public
name|void
name|setRequireContactInformation
parameter_list|(
specifier|final
name|boolean
name|r
parameter_list|)
block|{
name|requireContactInformation
operator|=
name|r
expr_stmt|;
block|}
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
name|shortName
return|;
block|}
DECL|method|getShortDescription ()
specifier|public
name|String
name|getShortDescription
parameter_list|()
block|{
return|return
name|shortDescription
return|;
block|}
DECL|method|setShortDescription (final String d)
specifier|public
name|void
name|setShortDescription
parameter_list|(
specifier|final
name|String
name|d
parameter_list|)
block|{
name|shortDescription
operator|=
name|d
expr_stmt|;
block|}
DECL|method|getAgreementHtml ()
specifier|public
name|String
name|getAgreementHtml
parameter_list|()
block|{
return|return
name|agreementHtml
return|;
block|}
DECL|method|setAgreementHtml (final String h)
specifier|public
name|void
name|setAgreementHtml
parameter_list|(
specifier|final
name|String
name|h
parameter_list|)
block|{
name|agreementHtml
operator|=
name|h
expr_stmt|;
block|}
block|}
end_class

end_unit

