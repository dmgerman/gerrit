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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|Account
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
name|ContactInformation
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
name|ReviewDb
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
name|rpc
operator|.
name|ContactInformationStoreException
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
name|server
operator|.
name|config
operator|.
name|SitePath
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
name|SchemaFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_class
DECL|class|EncryptedContactStoreProvider
class|class
name|EncryptedContactStoreProvider
implements|implements
name|Provider
argument_list|<
name|ContactStore
argument_list|>
block|{
annotation|@
name|Inject
DECL|field|server
specifier|private
name|GerritServer
name|server
decl_stmt|;
annotation|@
name|Inject
DECL|field|schema
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|SitePath
DECL|field|sitePath
specifier|private
name|File
name|sitePath
decl_stmt|;
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ContactStore
name|get
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|EncryptedContactStore
argument_list|(
name|server
argument_list|,
name|sitePath
argument_list|,
name|schema
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|ContactInformationStoreException
name|initError
parameter_list|)
block|{
return|return
operator|new
name|ContactStore
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|store
parameter_list|(
name|Account
name|account
parameter_list|,
name|ContactInformation
name|info
parameter_list|)
throws|throws
name|ContactInformationStoreException
block|{
throw|throw
name|initError
throw|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

