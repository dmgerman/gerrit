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

begin_comment
comment|/** Description of the SSH daemon host key used by Gerrit. */
end_comment

begin_class
DECL|class|SshHostKey
specifier|public
class|class
name|SshHostKey
block|{
DECL|field|hostIdent
specifier|protected
name|String
name|hostIdent
decl_stmt|;
DECL|field|hostKey
specifier|protected
name|String
name|hostKey
decl_stmt|;
DECL|field|fingerprint
specifier|protected
name|String
name|fingerprint
decl_stmt|;
DECL|method|SshHostKey ()
specifier|protected
name|SshHostKey
parameter_list|()
block|{   }
DECL|method|SshHostKey (final String hi, final String hk, final String fp)
specifier|public
name|SshHostKey
parameter_list|(
specifier|final
name|String
name|hi
parameter_list|,
specifier|final
name|String
name|hk
parameter_list|,
specifier|final
name|String
name|fp
parameter_list|)
block|{
name|hostIdent
operator|=
name|hi
expr_stmt|;
name|hostKey
operator|=
name|hk
expr_stmt|;
name|fingerprint
operator|=
name|fp
expr_stmt|;
block|}
comment|/** @return host name string, to appear in a known_hosts file. */
DECL|method|getHostIdent ()
specifier|public
name|String
name|getHostIdent
parameter_list|()
block|{
return|return
name|hostIdent
return|;
block|}
comment|/** @return base 64 encoded host key string, starting with key type. */
DECL|method|getHostKey ()
specifier|public
name|String
name|getHostKey
parameter_list|()
block|{
return|return
name|hostKey
return|;
block|}
comment|/** @return the key fingerprint, as displayed by a connecting client. */
DECL|method|getFingerprint ()
specifier|public
name|String
name|getFingerprint
parameter_list|()
block|{
return|return
name|fingerprint
return|;
block|}
block|}
end_class

end_unit

