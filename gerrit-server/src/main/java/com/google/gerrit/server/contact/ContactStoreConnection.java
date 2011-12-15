begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.contact
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|contact
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/** Single connection to a {@link ContactStore}. */
end_comment

begin_interface
DECL|interface|ContactStoreConnection
specifier|public
interface|interface
name|ContactStoreConnection
block|{
DECL|interface|Factory
specifier|public
specifier|static
interface|interface
name|Factory
block|{
comment|/**      * Open a new connection to a {@link ContactStore}.      *      * @param url contact store URL.      * @return a new connection to the store.      *      * @throws IOException the URL couldn't be opened.      */
DECL|method|open (URL url)
name|ContactStoreConnection
name|open
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
comment|/**    * Store a blob of contact data in the store.    *    * @param body protocol-specific body data.    *    * @throws IOException an error occurred storing the contact data.    */
DECL|method|store (byte[] body)
specifier|public
name|void
name|store
parameter_list|(
name|byte
index|[]
name|body
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

