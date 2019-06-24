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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|List
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
name|lib
operator|.
name|RefDatabase
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
name|Repository
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
name|revwalk
operator|.
name|RevWalk
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
name|AbstractAdvertiseRefsHook
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
comment|/**  * Wrapper around {@link com.google.gerrit.server.permissions.PermissionBackend.ForProject} that  * implements {@link org.eclipse.jgit.transport.AdvertiseRefsHook}.  */
end_comment

begin_class
DECL|class|DefaultAdvertiseRefsHook
specifier|public
class|class
name|DefaultAdvertiseRefsHook
extends|extends
name|AbstractAdvertiseRefsHook
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|perm
specifier|private
specifier|final
name|PermissionBackend
operator|.
name|ForProject
name|perm
decl_stmt|;
DECL|field|opts
specifier|private
specifier|final
name|PermissionBackend
operator|.
name|RefFilterOptions
name|opts
decl_stmt|;
DECL|method|DefaultAdvertiseRefsHook ( PermissionBackend.ForProject perm, PermissionBackend.RefFilterOptions opts)
specifier|public
name|DefaultAdvertiseRefsHook
parameter_list|(
name|PermissionBackend
operator|.
name|ForProject
name|perm
parameter_list|,
name|PermissionBackend
operator|.
name|RefFilterOptions
name|opts
parameter_list|)
block|{
name|this
operator|.
name|perm
operator|=
name|perm
expr_stmt|;
name|this
operator|.
name|opts
operator|=
name|opts
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAdvertisedRefs (Repository repo, RevWalk revWalk)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|getAdvertisedRefs
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|revWalk
parameter_list|)
throws|throws
name|ServiceMayNotContinueException
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"ref filter options = %s"
argument_list|,
name|opts
argument_list|)
expr_stmt|;
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
init|=
operator|!
name|opts
operator|.
name|prefixes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|?
name|opts
operator|.
name|prefixes
argument_list|()
else|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|RefDatabase
operator|.
name|ALL
argument_list|)
decl_stmt|;
return|return
name|perm
operator|.
name|filter
argument_list|(
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefsByPrefix
argument_list|(
name|prefixes
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
argument_list|,
name|repo
argument_list|,
name|opts
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|ServiceMayNotContinueException
name|ex
init|=
operator|new
name|ServiceMayNotContinueException
argument_list|()
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
block|}
end_class

end_unit

