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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|entities
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
name|entities
operator|.
name|AccountGroup
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
name|entities
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
name|entities
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
name|extensions
operator|.
name|config
operator|.
name|FactoryModule
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
name|args4j
operator|.
name|AccountGroupIdHandler
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
name|args4j
operator|.
name|AccountGroupUUIDHandler
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
name|args4j
operator|.
name|AccountIdHandler
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
name|args4j
operator|.
name|ChangeIdHandler
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
name|args4j
operator|.
name|ObjectIdHandler
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
name|args4j
operator|.
name|PatchSetIdHandler
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
name|args4j
operator|.
name|ProjectHandler
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
name|args4j
operator|.
name|SocketAddressHandler
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
name|args4j
operator|.
name|TimestampHandler
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
name|ProjectState
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
name|util
operator|.
name|cli
operator|.
name|CmdLineParser
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
name|util
operator|.
name|cli
operator|.
name|OptionHandlerUtil
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
name|util
operator|.
name|cli
operator|.
name|OptionHandlers
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|OptionHandler
import|;
end_import

begin_class
DECL|class|CmdLineParserModule
specifier|public
class|class
name|CmdLineParserModule
extends|extends
name|FactoryModule
block|{
DECL|method|CmdLineParserModule ()
specifier|public
name|CmdLineParserModule
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|factory
argument_list|(
name|CmdLineParser
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|OptionHandlers
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|Account
operator|.
name|Id
operator|.
name|class
argument_list|,
name|AccountIdHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|AccountGroup
operator|.
name|Id
operator|.
name|class
argument_list|,
name|AccountGroupIdHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|AccountGroup
operator|.
name|UUID
operator|.
name|class
argument_list|,
name|AccountGroupUUIDHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|Change
operator|.
name|Id
operator|.
name|class
argument_list|,
name|ChangeIdHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|ObjectId
operator|.
name|class
argument_list|,
name|ObjectIdHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|PatchSet
operator|.
name|Id
operator|.
name|class
argument_list|,
name|PatchSetIdHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|ProjectState
operator|.
name|class
argument_list|,
name|ProjectHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|SocketAddress
operator|.
name|class
argument_list|,
name|SocketAddressHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|Timestamp
operator|.
name|class
argument_list|,
name|TimestampHandler
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|registerOptionHandler (Class<T> type, Class<? extends OptionHandler<T>> impl)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|registerOptionHandler
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|OptionHandler
argument_list|<
name|T
argument_list|>
argument_list|>
name|impl
parameter_list|)
block|{
name|install
argument_list|(
name|OptionHandlerUtil
operator|.
name|moduleFor
argument_list|(
name|type
argument_list|,
name|impl
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

