begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|entities
operator|.
name|Project
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|ReceivePack
import|;
end_import

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ReceivePackInitializer
specifier|public
interface|interface
name|ReceivePackInitializer
block|{
comment|/**    * ReceivePack initialization.    *    *<p>Invoked by Gerrit when a new ReceivePack instance is created and just before it is used.    * Implementors will usually call setXXX methods on the receivePack parameter in order to set    * additional properties on it.    *    * @param project project for which the ReceivePack is created    * @param receivePack the ReceivePack instance which is being initialized    */
DECL|method|init (Project.NameKey project, ReceivePack receivePack)
name|void
name|init
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ReceivePack
name|receivePack
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

