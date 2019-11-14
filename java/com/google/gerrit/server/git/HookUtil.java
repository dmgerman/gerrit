begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toMap
import|;
end_import

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
name|Map
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
name|Ref
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
name|ServiceMayNotContinueException
import|;
end_import

begin_comment
comment|/** Static utilities for writing git protocol hooks. */
end_comment

begin_class
DECL|class|HookUtil
specifier|public
class|class
name|HookUtil
block|{
comment|/**    * Scan and advertise all refs in the repo if refs have not already been advertised; otherwise,    * just return the advertised map.    *    * @param rp receive-pack handler.    * @return map of refs that were advertised.    * @throws ServiceMayNotContinueException if a problem occurred.    */
DECL|method|ensureAllRefsAdvertised (ReceivePack rp)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|ensureAllRefsAdvertised
parameter_list|(
name|ReceivePack
name|rp
parameter_list|)
throws|throws
name|ServiceMayNotContinueException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
init|=
name|rp
operator|.
name|getAdvertisedRefs
argument_list|()
decl_stmt|;
if|if
condition|(
name|refs
operator|!=
literal|null
condition|)
block|{
return|return
name|refs
return|;
block|}
try|try
block|{
name|refs
operator|=
name|rp
operator|.
name|getRepository
argument_list|()
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toMap
argument_list|(
name|Ref
operator|::
name|getName
argument_list|,
name|r
lambda|->
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServiceMayNotContinueException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceMayNotContinueException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|rp
operator|.
name|setAdvertisedRefs
argument_list|(
name|refs
argument_list|,
name|rp
operator|.
name|getAdvertisedObjects
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|refs
return|;
block|}
DECL|method|HookUtil ()
specifier|private
name|HookUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

