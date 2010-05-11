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
DECL|package|com.google.gerrit.common.auth.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|auth
operator|.
name|openid
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
name|AccountExternalId
import|;
end_import

begin_class
DECL|class|OpenIdProviderPattern
specifier|public
class|class
name|OpenIdProviderPattern
block|{
DECL|method|create (String pattern)
specifier|public
specifier|static
name|OpenIdProviderPattern
name|create
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|OpenIdProviderPattern
name|r
init|=
operator|new
name|OpenIdProviderPattern
argument_list|()
decl_stmt|;
name|r
operator|.
name|regex
operator|=
name|pattern
operator|.
name|startsWith
argument_list|(
literal|"^"
argument_list|)
operator|&&
name|pattern
operator|.
name|endsWith
argument_list|(
literal|"$"
argument_list|)
expr_stmt|;
name|r
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|field|regex
specifier|protected
name|boolean
name|regex
decl_stmt|;
DECL|field|pattern
specifier|protected
name|String
name|pattern
decl_stmt|;
DECL|method|OpenIdProviderPattern ()
specifier|protected
name|OpenIdProviderPattern
parameter_list|()
block|{   }
DECL|method|matches (String id)
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|regex
condition|?
name|id
operator|.
name|matches
argument_list|(
name|pattern
argument_list|)
else|:
name|id
operator|.
name|startsWith
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|matches (AccountExternalId id)
specifier|public
name|boolean
name|matches
parameter_list|(
name|AccountExternalId
name|id
parameter_list|)
block|{
return|return
name|matches
argument_list|(
name|id
operator|.
name|getExternalId
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
name|pattern
return|;
block|}
block|}
end_class

end_unit

