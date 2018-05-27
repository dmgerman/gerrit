begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.util.ssl
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|ssl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSession
import|;
end_import

begin_comment
comment|/** HostnameVerifier that ignores host name. */
end_comment

begin_class
DECL|class|BlindHostnameVerifier
specifier|public
class|class
name|BlindHostnameVerifier
implements|implements
name|HostnameVerifier
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|HostnameVerifier
name|INSTANCE
init|=
operator|new
name|BlindHostnameVerifier
argument_list|()
decl_stmt|;
DECL|method|getInstance ()
specifier|public
specifier|static
name|HostnameVerifier
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
annotation|@
name|Override
DECL|method|verify (String hostname, SSLSession session)
specifier|public
name|boolean
name|verify
parameter_list|(
name|String
name|hostname
parameter_list|,
name|SSLSession
name|session
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

