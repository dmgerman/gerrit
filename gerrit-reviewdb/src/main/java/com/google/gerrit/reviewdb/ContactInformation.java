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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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

begin_comment
comment|/** Non-Internet contact details, such as a postal address and telephone. */
end_comment

begin_class
DECL|class|ContactInformation
specifier|public
specifier|final
class|class
name|ContactInformation
block|{
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|,
name|length
operator|=
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|address
specifier|protected
name|String
name|address
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|,
name|notNull
operator|=
literal|false
argument_list|,
name|length
operator|=
literal|40
argument_list|)
DECL|field|country
specifier|protected
name|String
name|country
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|,
name|notNull
operator|=
literal|false
argument_list|,
name|length
operator|=
literal|30
argument_list|)
DECL|field|phoneNbr
specifier|protected
name|String
name|phoneNbr
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|,
name|notNull
operator|=
literal|false
argument_list|,
name|length
operator|=
literal|30
argument_list|)
DECL|field|faxNbr
specifier|protected
name|String
name|faxNbr
decl_stmt|;
DECL|method|ContactInformation ()
specifier|public
name|ContactInformation
parameter_list|()
block|{   }
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|setAddress (final String a)
specifier|public
name|void
name|setAddress
parameter_list|(
specifier|final
name|String
name|a
parameter_list|)
block|{
name|address
operator|=
name|a
expr_stmt|;
block|}
DECL|method|getCountry ()
specifier|public
name|String
name|getCountry
parameter_list|()
block|{
return|return
name|country
return|;
block|}
DECL|method|setCountry (final String c)
specifier|public
name|void
name|setCountry
parameter_list|(
specifier|final
name|String
name|c
parameter_list|)
block|{
name|country
operator|=
name|c
expr_stmt|;
block|}
DECL|method|getPhoneNumber ()
specifier|public
name|String
name|getPhoneNumber
parameter_list|()
block|{
return|return
name|phoneNbr
return|;
block|}
DECL|method|setPhoneNumber (final String p)
specifier|public
name|void
name|setPhoneNumber
parameter_list|(
specifier|final
name|String
name|p
parameter_list|)
block|{
name|phoneNbr
operator|=
name|p
expr_stmt|;
block|}
DECL|method|getFaxNumber ()
specifier|public
name|String
name|getFaxNumber
parameter_list|()
block|{
return|return
name|faxNbr
return|;
block|}
DECL|method|setFaxNumber (final String f)
specifier|public
name|void
name|setFaxNumber
parameter_list|(
specifier|final
name|String
name|f
parameter_list|)
block|{
name|faxNbr
operator|=
name|f
expr_stmt|;
block|}
DECL|method|hasData (final ContactInformation contactInformation)
specifier|public
specifier|static
name|boolean
name|hasData
parameter_list|(
specifier|final
name|ContactInformation
name|contactInformation
parameter_list|)
block|{
if|if
condition|(
name|contactInformation
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|hasData
argument_list|(
name|contactInformation
operator|.
name|address
argument_list|)
operator|||
name|hasData
argument_list|(
name|contactInformation
operator|.
name|country
argument_list|)
operator|||
name|hasData
argument_list|(
name|contactInformation
operator|.
name|phoneNbr
argument_list|)
operator|||
name|hasData
argument_list|(
name|contactInformation
operator|.
name|faxNbr
argument_list|)
return|;
block|}
DECL|method|hasAddress (final ContactInformation contactInformation)
specifier|public
specifier|static
name|boolean
name|hasAddress
parameter_list|(
specifier|final
name|ContactInformation
name|contactInformation
parameter_list|)
block|{
return|return
name|contactInformation
operator|!=
literal|null
operator|&&
name|hasData
argument_list|(
name|contactInformation
operator|.
name|address
argument_list|)
return|;
block|}
DECL|method|hasData (final String s)
specifier|private
specifier|static
name|boolean
name|hasData
parameter_list|(
specifier|final
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
return|;
block|}
block|}
end_class

end_unit

