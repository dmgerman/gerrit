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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
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
name|joining
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
name|config
operator|.
name|GerritServerConfig
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
name|mail
operator|.
name|receive
operator|.
name|MailMessage
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|Config
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

begin_class
annotation|@
name|Singleton
DECL|class|ListMailFilter
specifier|public
class|class
name|ListMailFilter
implements|implements
name|MailFilter
block|{
DECL|enum|ListFilterMode
specifier|public
enum|enum
name|ListFilterMode
block|{
DECL|enumConstant|OFF
name|OFF
block|,
DECL|enumConstant|WHITELIST
name|WHITELIST
block|,
DECL|enumConstant|BLACKLIST
name|BLACKLIST
block|}
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
name|ListMailFilter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|mode
specifier|private
specifier|final
name|ListFilterMode
name|mode
decl_stmt|;
DECL|field|mailPattern
specifier|private
specifier|final
name|Pattern
name|mailPattern
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListMailFilter (@erritServerConfig Config cfg)
name|ListMailFilter
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|cfg
operator|.
name|getEnum
argument_list|(
literal|"receiveemail"
argument_list|,
literal|"filter"
argument_list|,
literal|"mode"
argument_list|,
name|ListFilterMode
operator|.
name|OFF
argument_list|)
expr_stmt|;
name|String
index|[]
name|addresses
init|=
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"receiveemail"
argument_list|,
literal|"filter"
argument_list|,
literal|"patterns"
argument_list|)
decl_stmt|;
name|String
name|concat
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|addresses
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|"|"
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|mailPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|concat
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|shouldProcessMessage (MailMessage message)
specifier|public
name|boolean
name|shouldProcessMessage
parameter_list|(
name|MailMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|mode
operator|==
name|ListFilterMode
operator|.
name|OFF
condition|)
block|{
return|return
literal|true
return|;
block|}
name|boolean
name|match
init|=
name|mailPattern
operator|.
name|matcher
argument_list|(
name|message
operator|.
name|from
argument_list|()
operator|.
name|email
argument_list|)
operator|.
name|find
argument_list|()
decl_stmt|;
if|if
condition|(
name|mode
operator|==
name|ListFilterMode
operator|.
name|WHITELIST
operator|&&
operator|!
name|match
operator|||
name|mode
operator|==
name|ListFilterMode
operator|.
name|BLACKLIST
operator|&&
name|match
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Mail message from "
operator|+
name|message
operator|.
name|from
argument_list|()
operator|+
literal|" rejected by list filter"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

