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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|common
operator|.
name|Nullable
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
name|entities
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
name|server
operator|.
name|AccessPath
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
name|CurrentUser
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
name|account
operator|.
name|AuthResult
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
name|account
operator|.
name|externalids
operator|.
name|ExternalId
import|;
end_import

begin_interface
DECL|interface|WebSession
specifier|public
interface|interface
name|WebSession
block|{
DECL|method|isSignedIn ()
name|boolean
name|isSignedIn
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|getXGerritAuth ()
name|String
name|getXGerritAuth
parameter_list|()
function_decl|;
DECL|method|isValidXGerritAuth (String keyIn)
name|boolean
name|isValidXGerritAuth
parameter_list|(
name|String
name|keyIn
parameter_list|)
function_decl|;
DECL|method|getLastLoginExternalId ()
name|ExternalId
operator|.
name|Key
name|getLastLoginExternalId
parameter_list|()
function_decl|;
DECL|method|getUser ()
name|CurrentUser
name|getUser
parameter_list|()
function_decl|;
DECL|method|login (AuthResult res, boolean rememberMe)
name|void
name|login
parameter_list|(
name|AuthResult
name|res
parameter_list|,
name|boolean
name|rememberMe
parameter_list|)
function_decl|;
comment|/** Set the user account for this current request only. */
DECL|method|setUserAccountId (Account.Id id)
name|void
name|setUserAccountId
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
DECL|method|isAccessPathOk (AccessPath path)
name|boolean
name|isAccessPathOk
parameter_list|(
name|AccessPath
name|path
parameter_list|)
function_decl|;
DECL|method|setAccessPathOk (AccessPath path, boolean ok)
name|void
name|setAccessPathOk
parameter_list|(
name|AccessPath
name|path
parameter_list|,
name|boolean
name|ok
parameter_list|)
function_decl|;
DECL|method|logout ()
name|void
name|logout
parameter_list|()
function_decl|;
DECL|method|getSessionId ()
name|String
name|getSessionId
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

