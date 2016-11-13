begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|util
operator|.
name|Optional
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
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_comment
comment|/**  * Simple short-lived cache of individual refs read from a repo.  *  *<p>Within a single request that is known to read a small bounded number of refs, this class can  * be used to ensure a consistent view of one ref, and avoid multiple system calls to read refs  * multiple times.  *  *<p><strong>Note:</strong> Implementations of this class are only appropriate for short-term  * caching, and do not support invalidation. It is also not threadsafe.  */
end_comment

begin_interface
DECL|interface|RefCache
specifier|public
interface|interface
name|RefCache
block|{
comment|/**    * Get the possibly-cached value of a ref.    *    * @param refName name of the ref.    * @return value of the ref; absent if the ref does not exist in the repo. Never null, and never    *     present with a value of {@link ObjectId#zeroId()}.    */
DECL|method|get (String refName)
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|get
parameter_list|(
name|String
name|refName
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

