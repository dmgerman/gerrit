begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|NotifyHandling
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
name|common
operator|.
name|AccountInfo
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
name|events
operator|.
name|GitReferenceUpdatedListener
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
name|registration
operator|.
name|DynamicSet
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
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
name|inject
operator|.
name|Inject
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
name|BatchRefUpdate
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
name|RefUpdate
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
name|ReceiveCommand
import|;
end_import

begin_class
DECL|class|GitReferenceUpdated
specifier|public
class|class
name|GitReferenceUpdated
block|{
DECL|field|DISABLED
specifier|public
specifier|static
specifier|final
name|GitReferenceUpdated
name|DISABLED
init|=
operator|new
name|GitReferenceUpdated
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|RefUpdate
name|refUpdate
parameter_list|,
name|ReceiveCommand
operator|.
name|Type
name|type
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{}
annotation|@
name|Override
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|RefUpdate
name|refUpdate
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{}
annotation|@
name|Override
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|,
name|ObjectId
name|oldObjectId
parameter_list|,
name|ObjectId
name|newObjectId
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{}
annotation|@
name|Override
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ReceiveCommand
name|cmd
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{}
annotation|@
name|Override
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|BatchRefUpdate
name|batchRefUpdate
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{}
block|}
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|GitReferenceUpdatedListener
argument_list|>
name|listeners
decl_stmt|;
DECL|field|util
specifier|private
specifier|final
name|EventUtil
name|util
decl_stmt|;
annotation|@
name|Inject
DECL|method|GitReferenceUpdated (DynamicSet<GitReferenceUpdatedListener> listeners, EventUtil util)
name|GitReferenceUpdated
parameter_list|(
name|DynamicSet
argument_list|<
name|GitReferenceUpdatedListener
argument_list|>
name|listeners
parameter_list|,
name|EventUtil
name|util
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
name|this
operator|.
name|util
operator|=
name|util
expr_stmt|;
block|}
DECL|method|GitReferenceUpdated ()
specifier|private
name|GitReferenceUpdated
parameter_list|()
block|{
name|this
operator|.
name|listeners
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|util
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|fire ( Project.NameKey project, RefUpdate refUpdate, ReceiveCommand.Type type, Account updater)
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|RefUpdate
name|refUpdate
parameter_list|,
name|ReceiveCommand
operator|.
name|Type
name|type
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{
name|fire
argument_list|(
name|project
argument_list|,
name|refUpdate
operator|.
name|getName
argument_list|()
argument_list|,
name|refUpdate
operator|.
name|getOldObjectId
argument_list|()
argument_list|,
name|refUpdate
operator|.
name|getNewObjectId
argument_list|()
argument_list|,
name|type
argument_list|,
name|util
operator|.
name|accountInfo
argument_list|(
name|updater
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fire (Project.NameKey project, RefUpdate refUpdate, Account updater)
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|RefUpdate
name|refUpdate
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{
name|fire
argument_list|(
name|project
argument_list|,
name|refUpdate
operator|.
name|getName
argument_list|()
argument_list|,
name|refUpdate
operator|.
name|getOldObjectId
argument_list|()
argument_list|,
name|refUpdate
operator|.
name|getNewObjectId
argument_list|()
argument_list|,
name|ReceiveCommand
operator|.
name|Type
operator|.
name|UPDATE
argument_list|,
name|util
operator|.
name|accountInfo
argument_list|(
name|updater
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fire ( Project.NameKey project, String ref, ObjectId oldObjectId, ObjectId newObjectId, Account updater)
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|,
name|ObjectId
name|oldObjectId
parameter_list|,
name|ObjectId
name|newObjectId
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{
name|fire
argument_list|(
name|project
argument_list|,
name|ref
argument_list|,
name|oldObjectId
argument_list|,
name|newObjectId
argument_list|,
name|ReceiveCommand
operator|.
name|Type
operator|.
name|UPDATE
argument_list|,
name|util
operator|.
name|accountInfo
argument_list|(
name|updater
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fire (Project.NameKey project, ReceiveCommand cmd, Account updater)
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ReceiveCommand
name|cmd
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{
name|fire
argument_list|(
name|project
argument_list|,
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|,
name|cmd
operator|.
name|getOldId
argument_list|()
argument_list|,
name|cmd
operator|.
name|getNewId
argument_list|()
argument_list|,
name|cmd
operator|.
name|getType
argument_list|()
argument_list|,
name|util
operator|.
name|accountInfo
argument_list|(
name|updater
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fire (Project.NameKey project, BatchRefUpdate batchRefUpdate, Account updater)
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|BatchRefUpdate
name|batchRefUpdate
parameter_list|,
name|Account
name|updater
parameter_list|)
block|{
if|if
condition|(
operator|!
name|listeners
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ReceiveCommand
name|cmd
range|:
name|batchRefUpdate
operator|.
name|getCommands
argument_list|()
control|)
block|{
if|if
condition|(
name|cmd
operator|.
name|getResult
argument_list|()
operator|==
name|ReceiveCommand
operator|.
name|Result
operator|.
name|OK
condition|)
block|{
name|fire
argument_list|(
name|project
argument_list|,
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|,
name|cmd
operator|.
name|getOldId
argument_list|()
argument_list|,
name|cmd
operator|.
name|getNewId
argument_list|()
argument_list|,
name|cmd
operator|.
name|getType
argument_list|()
argument_list|,
name|util
operator|.
name|accountInfo
argument_list|(
name|updater
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|fire ( Project.NameKey project, String ref, ObjectId oldObjectId, ObjectId newObjectId, ReceiveCommand.Type type, AccountInfo updater)
specifier|private
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|,
name|ObjectId
name|oldObjectId
parameter_list|,
name|ObjectId
name|newObjectId
parameter_list|,
name|ReceiveCommand
operator|.
name|Type
name|type
parameter_list|,
name|AccountInfo
name|updater
parameter_list|)
block|{
if|if
condition|(
operator|!
name|listeners
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return;
block|}
name|ObjectId
name|o
init|=
name|oldObjectId
operator|!=
literal|null
condition|?
name|oldObjectId
else|:
name|ObjectId
operator|.
name|zeroId
argument_list|()
decl_stmt|;
name|ObjectId
name|n
init|=
name|newObjectId
operator|!=
literal|null
condition|?
name|newObjectId
else|:
name|ObjectId
operator|.
name|zeroId
argument_list|()
decl_stmt|;
name|Event
name|event
init|=
operator|new
name|Event
argument_list|(
name|project
argument_list|,
name|ref
argument_list|,
name|o
operator|.
name|name
argument_list|()
argument_list|,
name|n
operator|.
name|name
argument_list|()
argument_list|,
name|type
argument_list|,
name|updater
argument_list|)
decl_stmt|;
for|for
control|(
name|GitReferenceUpdatedListener
name|l
range|:
name|listeners
control|)
block|{
try|try
block|{
name|l
operator|.
name|onGitReferenceUpdated
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|util
operator|.
name|logEventListenerError
argument_list|(
name|this
argument_list|,
name|l
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|Event
specifier|private
specifier|static
class|class
name|Event
implements|implements
name|GitReferenceUpdatedListener
operator|.
name|Event
block|{
DECL|field|projectName
specifier|private
specifier|final
name|String
name|projectName
decl_stmt|;
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|field|oldObjectId
specifier|private
specifier|final
name|String
name|oldObjectId
decl_stmt|;
DECL|field|newObjectId
specifier|private
specifier|final
name|String
name|newObjectId
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|ReceiveCommand
operator|.
name|Type
name|type
decl_stmt|;
DECL|field|updater
specifier|private
specifier|final
name|AccountInfo
name|updater
decl_stmt|;
DECL|method|Event ( Project.NameKey project, String ref, String oldObjectId, String newObjectId, ReceiveCommand.Type type, AccountInfo updater)
name|Event
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|,
name|String
name|oldObjectId
parameter_list|,
name|String
name|newObjectId
parameter_list|,
name|ReceiveCommand
operator|.
name|Type
name|type
parameter_list|,
name|AccountInfo
name|updater
parameter_list|)
block|{
name|this
operator|.
name|projectName
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|this
operator|.
name|oldObjectId
operator|=
name|oldObjectId
expr_stmt|;
name|this
operator|.
name|newObjectId
operator|=
name|newObjectId
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|updater
operator|=
name|updater
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProjectName ()
specifier|public
name|String
name|getProjectName
parameter_list|()
block|{
return|return
name|projectName
return|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
annotation|@
name|Override
DECL|method|getOldObjectId ()
specifier|public
name|String
name|getOldObjectId
parameter_list|()
block|{
return|return
name|oldObjectId
return|;
block|}
annotation|@
name|Override
DECL|method|getNewObjectId ()
specifier|public
name|String
name|getNewObjectId
parameter_list|()
block|{
return|return
name|newObjectId
return|;
block|}
annotation|@
name|Override
DECL|method|isCreate ()
specifier|public
name|boolean
name|isCreate
parameter_list|()
block|{
return|return
name|type
operator|==
name|ReceiveCommand
operator|.
name|Type
operator|.
name|CREATE
return|;
block|}
annotation|@
name|Override
DECL|method|isDelete ()
specifier|public
name|boolean
name|isDelete
parameter_list|()
block|{
return|return
name|type
operator|==
name|ReceiveCommand
operator|.
name|Type
operator|.
name|DELETE
return|;
block|}
annotation|@
name|Override
DECL|method|isNonFastForward ()
specifier|public
name|boolean
name|isNonFastForward
parameter_list|()
block|{
return|return
name|type
operator|==
name|ReceiveCommand
operator|.
name|Type
operator|.
name|UPDATE_NONFASTFORWARD
return|;
block|}
annotation|@
name|Override
DECL|method|getUpdater ()
specifier|public
name|AccountInfo
name|getUpdater
parameter_list|()
block|{
return|return
name|updater
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s[%s,%s: %s -> %s]"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|projectName
argument_list|,
name|ref
argument_list|,
name|oldObjectId
argument_list|,
name|newObjectId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getNotify ()
specifier|public
name|NotifyHandling
name|getNotify
parameter_list|()
block|{
return|return
name|NotifyHandling
operator|.
name|ALL
return|;
block|}
block|}
block|}
end_class

end_unit
