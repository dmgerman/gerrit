begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
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
name|data
operator|.
name|SideBySidePatchDetail
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
name|data
operator|.
name|UnifiedPatchDetail
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
name|patches
operator|.
name|PatchDetailService
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
name|Patch
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
name|BaseServiceImplementation
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
name|git
operator|.
name|RepositoryCache
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
name|GerritServer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_class
DECL|class|PatchDetailServiceImpl
specifier|public
class|class
name|PatchDetailServiceImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|PatchDetailService
block|{
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|method|PatchDetailServiceImpl (final GerritServer gs)
specifier|public
name|PatchDetailServiceImpl
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|)
block|{
name|super
argument_list|(
name|gs
operator|.
name|getDatabase
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|=
name|gs
expr_stmt|;
block|}
DECL|method|sideBySidePatchDetail (final Patch.Key key, final AsyncCallback<SideBySidePatchDetail> callback)
specifier|public
name|void
name|sideBySidePatchDetail
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|key
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|SideBySidePatchDetail
argument_list|>
name|callback
parameter_list|)
block|{
specifier|final
name|RepositoryCache
name|rc
init|=
name|server
operator|.
name|getRepositoryCache
argument_list|()
decl_stmt|;
if|if
condition|(
name|rc
operator|==
literal|null
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"No Repository Cache configured"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|SideBySidePatchDetailAction
argument_list|(
name|rc
argument_list|,
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|unifiedPatchDetail (final Patch.Key key, final AsyncCallback<UnifiedPatchDetail> callback)
specifier|public
name|void
name|unifiedPatchDetail
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|key
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|UnifiedPatchDetail
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|UnifiedPatchDetailAction
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

