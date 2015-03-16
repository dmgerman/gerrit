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

begin_comment
comment|/* OAuth token */
end_comment

begin_class
DECL|class|OAuthToken
specifier|public
class|class
name|OAuthToken
block|{
DECL|field|token
specifier|private
specifier|final
name|String
name|token
decl_stmt|;
DECL|field|secret
specifier|private
specifier|final
name|String
name|secret
decl_stmt|;
DECL|field|raw
specifier|private
specifier|final
name|String
name|raw
decl_stmt|;
DECL|method|OAuthToken (String token, String secret, String raw)
specifier|public
name|OAuthToken
parameter_list|(
name|String
name|token
parameter_list|,
name|String
name|secret
parameter_list|,
name|String
name|raw
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
name|this
operator|.
name|raw
operator|=
name|raw
expr_stmt|;
block|}
DECL|method|getToken ()
specifier|public
name|String
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
DECL|method|getSecret ()
specifier|public
name|String
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
DECL|method|getRaw ()
specifier|public
name|String
name|getRaw
parameter_list|()
block|{
return|return
name|raw
return|;
block|}
block|}
end_class

end_unit

