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
DECL|package|com.google.gerrit.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
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
name|gpg
operator|.
name|api
operator|.
name|GpgApiModule
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
name|EnableSignedPush
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
DECL|class|GpgModule
specifier|public
class|class
name|GpgModule
extends|extends
name|FactoryModule
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
name|GpgModule
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|method|GpgModule (Config cfg)
specifier|public
name|GpgModule
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|boolean
name|configEnableSignedPush
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"enableSignedPush"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|boolean
name|configEditGpgKeys
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"editGpgKeys"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|boolean
name|havePgp
init|=
name|BouncyCastleUtil
operator|.
name|havePGP
argument_list|()
decl_stmt|;
name|boolean
name|enableSignedPush
init|=
name|configEnableSignedPush
operator|&&
name|havePgp
decl_stmt|;
name|bindConstant
argument_list|()
operator|.
name|annotatedWith
argument_list|(
name|EnableSignedPush
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|enableSignedPush
argument_list|)
expr_stmt|;
if|if
condition|(
name|configEnableSignedPush
operator|&&
operator|!
name|havePgp
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Bouncy Castle PGP not installed; signed push verification is disabled"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enableSignedPush
condition|)
block|{
name|install
argument_list|(
operator|new
name|SignedPushModule
argument_list|()
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|GerritPushCertificateChecker
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|install
argument_list|(
operator|new
name|GpgApiModule
argument_list|(
name|enableSignedPush
operator|&&
name|configEditGpgKeys
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

