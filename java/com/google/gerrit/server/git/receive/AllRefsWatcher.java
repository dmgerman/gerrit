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
DECL|package|com.google.gerrit.server.git.receive
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
operator|.
name|receive
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|git
operator|.
name|HookUtil
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

begin_comment
comment|/**  * Hook that scans all refs and holds onto the results reference.  *  *<p>This allows a caller who has an {@code AllRefsWatcher} instance to get the full map of refs in  * the repo, even if refs are filtered by a later hook or filter.  */
end_comment

begin_class
DECL|class|AllRefsWatcher
class|class
name|AllRefsWatcher
implements|implements
name|AdvertiseRefsHook
block|{
DECL|field|allRefs
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|allRefs
decl_stmt|;
annotation|@
name|Override
DECL|method|advertiseRefs (ReceivePack rp)
specifier|public
name|void
name|advertiseRefs
parameter_list|(
name|ReceivePack
name|rp
parameter_list|)
throws|throws
name|ServiceMayNotContinueException
block|{
name|allRefs
operator|=
name|HookUtil
operator|.
name|ensureAllRefsAdvertised
argument_list|(
name|rp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|advertiseRefs (UploadPack uploadPack)
specifier|public
name|void
name|advertiseRefs
parameter_list|(
name|UploadPack
name|uploadPack
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|getAllRefs ()
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|getAllRefs
parameter_list|()
block|{
name|checkState
argument_list|(
name|allRefs
operator|!=
literal|null
argument_list|,
literal|"getAllRefs() only valid after refs were advertised"
argument_list|)
expr_stmt|;
return|return
name|allRefs
return|;
block|}
block|}
end_class

end_unit

