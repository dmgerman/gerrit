begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|AdvertiseRefsHook
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
name|BaseReceivePack
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
name|UploadPack
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/** Exposes only the non refs/changes/ reference names. */
end_comment

begin_class
DECL|class|ReceiveCommitsAdvertiseRefsHook
specifier|public
class|class
name|ReceiveCommitsAdvertiseRefsHook
implements|implements
name|AdvertiseRefsHook
block|{
annotation|@
name|Override
DECL|method|advertiseRefs (UploadPack us)
specifier|public
name|void
name|advertiseRefs
parameter_list|(
name|UploadPack
name|us
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"ReceiveCommitsAdvertiseRefsHook cannot be used for UploadPack"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|advertiseRefs (BaseReceivePack rp)
specifier|public
name|void
name|advertiseRefs
parameter_list|(
name|BaseReceivePack
name|rp
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|oldRefs
init|=
name|rp
operator|.
name|getAdvertisedRefs
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldRefs
operator|==
literal|null
condition|)
block|{
name|oldRefs
operator|=
name|rp
operator|.
name|getRepository
argument_list|()
operator|.
name|getAllRefs
argument_list|()
expr_stmt|;
block|}
name|HashMap
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|r
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|e
range|:
name|oldRefs
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"refs/changes/"
argument_list|)
condition|)
block|{
name|r
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|rp
operator|.
name|setAdvertisedRefs
argument_list|(
name|r
argument_list|,
name|rp
operator|.
name|getAdvertisedObjects
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

