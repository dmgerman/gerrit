begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|client
operator|.
name|ListChangesOption
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
name|common
operator|.
name|ApprovalInfo
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
name|ChangeInfo
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
name|RevisionInfo
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
name|Change
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
name|PatchSet
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|GpgException
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
name|change
operator|.
name|ChangeJson
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
name|patch
operator|.
name|PatchListNotAvailableException
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
name|project
operator|.
name|ChangeControl
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
name|query
operator|.
name|change
operator|.
name|ChangeData
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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

begin_class
DECL|class|EventUtil
specifier|public
class|class
name|EventUtil
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EventUtil
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|changeJson
specifier|private
specifier|final
name|ChangeJson
name|changeJson
decl_stmt|;
annotation|@
name|Inject
DECL|method|EventUtil (ChangeJson.Factory changeJsonFactory, ChangeData.Factory changeDataFactory, Provider<ReviewDb> db)
name|EventUtil
parameter_list|(
name|ChangeJson
operator|.
name|Factory
name|changeJsonFactory
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
block|{
name|this
operator|.
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|changeJson
operator|=
name|changeJsonFactory
operator|.
name|create
argument_list|(
name|EnumSet
operator|.
name|allOf
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|changeInfo (Change change)
specifier|public
name|ChangeInfo
name|changeInfo
parameter_list|(
name|Change
name|change
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|changeJson
operator|.
name|format
argument_list|(
name|change
argument_list|)
return|;
block|}
DECL|method|revisionInfo (Project project, PatchSet ps)
specifier|public
name|RevisionInfo
name|revisionInfo
parameter_list|(
name|Project
name|project
parameter_list|,
name|PatchSet
name|ps
parameter_list|)
throws|throws
name|OrmException
throws|,
name|PatchListNotAvailableException
throws|,
name|GpgException
throws|,
name|IOException
block|{
return|return
name|revisionInfo
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|ps
argument_list|)
return|;
block|}
DECL|method|revisionInfo (Project.NameKey project, PatchSet ps)
specifier|public
name|RevisionInfo
name|revisionInfo
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|PatchSet
name|ps
parameter_list|)
throws|throws
name|OrmException
throws|,
name|PatchListNotAvailableException
throws|,
name|GpgException
throws|,
name|IOException
block|{
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|project
argument_list|,
name|ps
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
name|ChangeControl
name|ctl
init|=
name|cd
operator|.
name|changeControl
argument_list|()
decl_stmt|;
return|return
name|changeJson
operator|.
name|getRevisionInfo
argument_list|(
name|ctl
argument_list|,
name|ps
argument_list|)
return|;
block|}
DECL|method|accountInfo (Account a)
specifier|public
name|AccountInfo
name|accountInfo
parameter_list|(
name|Account
name|a
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
literal|null
operator|||
name|a
operator|.
name|getId
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|AccountInfo
name|ai
init|=
operator|new
name|AccountInfo
argument_list|(
name|a
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|ai
operator|.
name|email
operator|=
name|a
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
name|ai
operator|.
name|name
operator|=
name|a
operator|.
name|getFullName
argument_list|()
expr_stmt|;
name|ai
operator|.
name|username
operator|=
name|a
operator|.
name|getUserName
argument_list|()
expr_stmt|;
return|return
name|ai
return|;
block|}
DECL|method|approvals (Account a, Map<String, Short> approvals, Timestamp ts)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ApprovalInfo
argument_list|>
name|approvals
parameter_list|(
name|Account
name|a
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|approvals
parameter_list|,
name|Timestamp
name|ts
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ApprovalInfo
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
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
name|Short
argument_list|>
name|e
range|:
name|approvals
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Integer
name|value
init|=
name|e
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|?
operator|new
name|Integer
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|ChangeJson
operator|.
name|getApprovalInfo
argument_list|(
name|a
operator|.
name|getId
argument_list|()
argument_list|,
name|value
argument_list|,
literal|null
argument_list|,
name|ts
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|logEventListenerError (Object event, Object listener, Exception error)
specifier|public
name|void
name|logEventListenerError
parameter_list|(
name|Object
name|event
parameter_list|,
name|Object
name|listener
parameter_list|,
name|Exception
name|error
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error in event listener %s for event %s"
argument_list|,
name|listener
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|event
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error in listener {} for event {}: {}"
argument_list|,
name|listener
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|event
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|error
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

