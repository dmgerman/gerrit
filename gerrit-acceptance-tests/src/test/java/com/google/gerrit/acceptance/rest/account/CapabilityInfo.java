begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|account
package|;
end_package

begin_class
DECL|class|CapabilityInfo
class|class
name|CapabilityInfo
block|{
DECL|field|accessDatabase
specifier|public
name|boolean
name|accessDatabase
decl_stmt|;
DECL|field|administrateServer
specifier|public
name|boolean
name|administrateServer
decl_stmt|;
DECL|field|createAccount
specifier|public
name|boolean
name|createAccount
decl_stmt|;
DECL|field|createGroup
specifier|public
name|boolean
name|createGroup
decl_stmt|;
DECL|field|createProject
specifier|public
name|boolean
name|createProject
decl_stmt|;
DECL|field|emailReviewers
specifier|public
name|boolean
name|emailReviewers
decl_stmt|;
DECL|field|flushCaches
specifier|public
name|boolean
name|flushCaches
decl_stmt|;
DECL|field|generateHttpPassword
specifier|public
name|boolean
name|generateHttpPassword
decl_stmt|;
DECL|field|killTask
specifier|public
name|boolean
name|killTask
decl_stmt|;
DECL|field|priority
specifier|public
name|boolean
name|priority
decl_stmt|;
DECL|field|queryLimit
specifier|public
name|QueryLimit
name|queryLimit
decl_stmt|;
DECL|field|runAs
specifier|public
name|boolean
name|runAs
decl_stmt|;
DECL|field|runGC
specifier|public
name|boolean
name|runGC
decl_stmt|;
DECL|field|streamEvents
specifier|public
name|boolean
name|streamEvents
decl_stmt|;
DECL|field|viewCaches
specifier|public
name|boolean
name|viewCaches
decl_stmt|;
DECL|field|viewConnections
specifier|public
name|boolean
name|viewConnections
decl_stmt|;
DECL|field|viewQueue
specifier|public
name|boolean
name|viewQueue
decl_stmt|;
DECL|class|QueryLimit
specifier|static
class|class
name|QueryLimit
block|{
DECL|field|min
name|short
name|min
decl_stmt|;
DECL|field|max
name|short
name|max
decl_stmt|;
block|}
block|}
end_class

end_unit

