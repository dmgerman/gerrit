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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|git
operator|.
name|ObjectIds
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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|revwalk
operator|.
name|RevCommit
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
comment|/**  * Advertises part of history to git push clients.  *  *<p>This is a hack to work around the lack of negotiation in the send-pack/receive-pack wire  * protocol.  *  *<p>When the server is frequently advancing master by creating merge commits, the client may not  * be able to discover a common ancestor during push. Attempting to push will re-upload a very large  * amount of history. This hook hacks in a fake negotiation replacement by walking history and  * sending recent commits as {@code ".have"} lines in the wire protocol, allowing the client to find  * a common ancestor.  */
end_comment

begin_class
DECL|class|HackPushNegotiateHook
specifier|public
class|class
name|HackPushNegotiateHook
implements|implements
name|AdvertiseRefsHook
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
comment|/** Size of an additional ".have" line. */
DECL|field|HAVE_LINE_LEN
specifier|private
specifier|static
specifier|final
name|int
name|HAVE_LINE_LEN
init|=
literal|4
operator|+
name|ObjectIds
operator|.
name|STR_LEN
operator|+
literal|1
operator|+
literal|5
operator|+
literal|1
decl_stmt|;
comment|/**    * Maximum number of bytes to "waste" in the advertisement with a peek at this repository's    * current reachable history.    */
DECL|field|MAX_EXTRA_BYTES
specifier|private
specifier|static
specifier|final
name|int
name|MAX_EXTRA_BYTES
init|=
literal|8192
decl_stmt|;
comment|/**    * Number of recent commits to advertise immediately, hoping to show a client a nearby merge base.    */
DECL|field|BASE_COMMITS
specifier|private
specifier|static
specifier|final
name|int
name|BASE_COMMITS
init|=
literal|64
decl_stmt|;
comment|/** Number of commits to skip once base has already been shown. */
DECL|field|STEP_COMMITS
specifier|private
specifier|static
specifier|final
name|int
name|STEP_COMMITS
init|=
literal|16
decl_stmt|;
comment|/** Total number of commits to extract from the history. */
DECL|field|MAX_HISTORY
specifier|private
specifier|static
specifier|final
name|int
name|MAX_HISTORY
init|=
name|MAX_EXTRA_BYTES
operator|/
name|HAVE_LINE_LEN
decl_stmt|;
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
literal|"HackPushNegotiateHook cannot be used for UploadPack"
argument_list|)
throw|;
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|r
init|=
name|rp
operator|.
name|getAdvertisedRefs
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|r
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
name|x
lambda|->
name|x
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
block|}
name|rp
operator|.
name|setAdvertisedRefs
argument_list|(
name|r
argument_list|,
name|history
argument_list|(
name|r
operator|.
name|values
argument_list|()
argument_list|,
name|rp
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|history (Collection<Ref> refs, ReceivePack rp)
specifier|private
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|history
parameter_list|(
name|Collection
argument_list|<
name|Ref
argument_list|>
name|refs
parameter_list|,
name|ReceivePack
name|rp
parameter_list|)
block|{
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|alreadySending
init|=
name|rp
operator|.
name|getAdvertisedObjects
argument_list|()
decl_stmt|;
if|if
condition|(
name|MAX_HISTORY
operator|<=
name|alreadySending
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|alreadySending
return|;
block|}
comment|// Scan history until the advertisement is full.
name|RevWalk
name|rw
init|=
name|rp
operator|.
name|getRevWalk
argument_list|()
decl_stmt|;
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
try|try
block|{
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|tips
init|=
name|idsOf
argument_list|(
name|refs
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjectId
name|tip
range|:
name|tips
control|)
block|{
try|try
block|{
name|rw
operator|.
name|markStart
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|tip
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|badCommit
parameter_list|)
block|{
continue|continue;
block|}
block|}
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|history
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|MAX_HISTORY
argument_list|)
decl_stmt|;
name|history
operator|.
name|addAll
argument_list|(
name|alreadySending
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|stepCnt
init|=
literal|0
decl_stmt|;
for|for
control|(
name|RevCommit
name|c
init|;
name|history
operator|.
name|size
argument_list|()
operator|<
name|MAX_HISTORY
operator|&&
operator|(
name|c
operator|=
name|rw
operator|.
name|next
argument_list|()
operator|)
operator|!=
literal|null
condition|;
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getParentCount
argument_list|()
operator|<=
literal|1
operator|&&
operator|!
name|tips
operator|.
name|contains
argument_list|(
name|c
argument_list|)
operator|&&
operator|(
name|history
operator|.
name|size
argument_list|()
operator|<
name|BASE_COMMITS
operator|||
operator|(
operator|++
name|stepCnt
operator|%
name|STEP_COMMITS
operator|)
operator|==
literal|0
operator|)
condition|)
block|{
name|history
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"error trying to advertise history"
argument_list|)
expr_stmt|;
block|}
return|return
name|history
return|;
block|}
finally|finally
block|{
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|idsOf (Collection<Ref> refs)
specifier|private
specifier|static
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|idsOf
parameter_list|(
name|Collection
argument_list|<
name|Ref
argument_list|>
name|refs
parameter_list|)
block|{
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|r
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|refs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|refs
control|)
block|{
if|if
condition|(
name|ref
operator|.
name|getObjectId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

