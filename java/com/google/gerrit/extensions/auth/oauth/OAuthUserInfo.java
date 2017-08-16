begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.auth.oauth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|auth
operator|.
name|oauth
package|;
end_package

begin_class
DECL|class|OAuthUserInfo
specifier|public
class|class
name|OAuthUserInfo
block|{
DECL|field|externalId
specifier|private
specifier|final
name|String
name|externalId
decl_stmt|;
DECL|field|userName
specifier|private
specifier|final
name|String
name|userName
decl_stmt|;
DECL|field|emailAddress
specifier|private
specifier|final
name|String
name|emailAddress
decl_stmt|;
DECL|field|displayName
specifier|private
specifier|final
name|String
name|displayName
decl_stmt|;
DECL|field|claimedIdentity
specifier|private
specifier|final
name|String
name|claimedIdentity
decl_stmt|;
DECL|method|OAuthUserInfo ( String externalId, String userName, String emailAddress, String displayName, String claimedIdentity)
specifier|public
name|OAuthUserInfo
parameter_list|(
name|String
name|externalId
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|emailAddress
parameter_list|,
name|String
name|displayName
parameter_list|,
name|String
name|claimedIdentity
parameter_list|)
block|{
name|this
operator|.
name|externalId
operator|=
name|externalId
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|emailAddress
operator|=
name|emailAddress
expr_stmt|;
name|this
operator|.
name|displayName
operator|=
name|displayName
expr_stmt|;
name|this
operator|.
name|claimedIdentity
operator|=
name|claimedIdentity
expr_stmt|;
block|}
DECL|method|getExternalId ()
specifier|public
name|String
name|getExternalId
parameter_list|()
block|{
return|return
name|externalId
return|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|getEmailAddress ()
specifier|public
name|String
name|getEmailAddress
parameter_list|()
block|{
return|return
name|emailAddress
return|;
block|}
DECL|method|getDisplayName ()
specifier|public
name|String
name|getDisplayName
parameter_list|()
block|{
return|return
name|displayName
return|;
block|}
DECL|method|getClaimedIdentity ()
specifier|public
name|String
name|getClaimedIdentity
parameter_list|()
block|{
return|return
name|claimedIdentity
return|;
block|}
block|}
end_class

end_unit

