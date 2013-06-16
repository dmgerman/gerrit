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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|nls
operator|.
name|NLS
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
name|nls
operator|.
name|TranslationBundle
import|;
end_import

begin_class
DECL|class|CapabilityConstants
specifier|public
class|class
name|CapabilityConstants
extends|extends
name|TranslationBundle
block|{
DECL|method|get ()
specifier|public
specifier|static
name|CapabilityConstants
name|get
parameter_list|()
block|{
return|return
name|NLS
operator|.
name|getBundleFor
argument_list|(
name|CapabilityConstants
operator|.
name|class
argument_list|)
return|;
block|}
DECL|field|accessDatabase
specifier|public
name|String
name|accessDatabase
decl_stmt|;
DECL|field|administrateServer
specifier|public
name|String
name|administrateServer
decl_stmt|;
DECL|field|createAccount
specifier|public
name|String
name|createAccount
decl_stmt|;
DECL|field|createGroup
specifier|public
name|String
name|createGroup
decl_stmt|;
DECL|field|createProject
specifier|public
name|String
name|createProject
decl_stmt|;
DECL|field|emailReviewers
specifier|public
name|String
name|emailReviewers
decl_stmt|;
DECL|field|flushCaches
specifier|public
name|String
name|flushCaches
decl_stmt|;
DECL|field|killTask
specifier|public
name|String
name|killTask
decl_stmt|;
DECL|field|priority
specifier|public
name|String
name|priority
decl_stmt|;
DECL|field|queryLimit
specifier|public
name|String
name|queryLimit
decl_stmt|;
DECL|field|runAs
specifier|public
name|String
name|runAs
decl_stmt|;
DECL|field|runGC
specifier|public
name|String
name|runGC
decl_stmt|;
DECL|field|streamEvents
specifier|public
name|String
name|streamEvents
decl_stmt|;
DECL|field|viewCaches
specifier|public
name|String
name|viewCaches
decl_stmt|;
DECL|field|viewConnections
specifier|public
name|String
name|viewConnections
decl_stmt|;
DECL|field|viewQueue
specifier|public
name|String
name|viewQueue
decl_stmt|;
block|}
end_class

end_unit

