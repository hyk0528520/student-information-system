/**
 * 学生信息管理系统 - 主JavaScript文件
 */

// 页面加载完成后执行
$(document).ready(function() {
    console.log('学生信息管理系统已加载');

    // 初始化工具提示
    $('[data-bs-toggle="tooltip"]').tooltip();

    // 初始化弹出框
    $('[data-bs-toggle="popover"]').popover();

    // 自动隐藏警告框
    $('.alert').delay(5000).fadeOut('slow', function() {
        $(this).remove();
    });

    // 表单验证增强
    $('form').on('submit', function(e) {
        var form = $(this);
        var requiredFields = form.find('[required]');
        var isValid = true;

        requiredFields.each(function() {
            if ($(this).val().trim() === '') {
                $(this).addClass('is-invalid');
                isValid = false;
            } else {
                $(this).removeClass('is-invalid');
            }
        });

        if (!isValid) {
            e.preventDefault();
            showToast('请填写所有必填字段！', 'error');
        }
    });

    // 手机号格式验证
    $('#phone').on('blur', function() {
        var phone = $(this).val();
        var phoneRegex = /^1[3-9]\d{9}$/;
        if (phone && !phoneRegex.test(phone)) {
            showToast('请输入正确的手机号格式！', 'warning');
            $(this).addClass('is-invalid');
        } else {
            $(this).removeClass('is-invalid');
        }
    });

    // 身份证号验证
    $('#idCard').on('blur', function() {
        var idCard = $(this).val();
        if (idCard) {
            var idCardRegex = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            if (!idCardRegex.test(idCard)) {
                showToast('请输入正确的身份证号！', 'warning');
                $(this).addClass('is-invalid');
            } else {
                $(this).removeClass('is-invalid');
            }
        }
    });

    // 邮箱验证
    $('#email').on('blur', function() {
        var email = $(this).val();
        if (email) {
            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                showToast('请输入正确的邮箱格式！', 'warning');
                $(this).addClass('is-invalid');
            } else {
                $(this).removeClass('is-invalid');
            }
        }
    });

    // 学号自动生成（示例）
    $('#autoGenerateNo').on('click', function() {
        var year = new Date().getFullYear();
        var random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
        $('#studentNo').val(year + random);
    });
});

/**
 * 显示提示消息
 * @param {string} message 消息内容
 * @param {string} type 消息类型：success, error, warning, info
 */
function showToast(message, type = 'info') {
    // 创建toast元素
    var toastHtml = `
        <div class="toast align-items-center text-bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;

    // 添加到容器
    var toastContainer = $('#toastContainer');
    if (toastContainer.length === 0) {
        toastContainer = $('<div id="toastContainer" style="position: fixed; top: 20px; right: 20px; z-index: 9999;"></div>');
        $('body').append(toastContainer);
    }

    var toast = $(toastHtml).appendTo(toastContainer);

    // 显示toast
    var bsToast = new bootstrap.Toast(toast[0], {
        animation: true,
        autohide: true,
        delay: 3000
    });
    bsToast.show();

    // 隐藏后移除
    toast.on('hidden.bs.toast', function() {
        $(this).remove();
    });
}

/**
 * 确认删除操作
 * @param {string} url 删除URL
 * @param {string} message 确认消息
 */
function confirmDelete(url, message = '确定要删除吗？此操作不可恢复！') {
    if (confirm(message)) {
        window.location.href = url;
    }
}

/**
 * 加载统计数据
 */
function loadStatistics() {
    $.ajax({
        url: '/api/statistics',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            if (data.success) {
                // 更新统计数据
                $('#studentCount').text(data.studentCount || 0);
                $('#courseCount').text(data.courseCount || 0);
                $('#avgScore').text(data.avgScore || '0.0');
                $('#leaveCount').text(data.leaveCount || 0);
            }
        },
        error: function() {
            console.log('加载统计数据失败');
        }
    });
}

/**
 * 导出数据
 * @param {string} type 数据类型：students, courses, scores
 */
function exportData(type) {
    var loading = showLoading('正在导出数据...');

    $.ajax({
        url: '/api/export/' + type,
        type: 'GET',
        xhrFields: {
            responseType: 'blob'
        },
        success: function(data) {
            var blob = new Blob([data], { type: 'application/vnd.ms-excel' });
            var link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = type + '_' + new Date().getTime() + '.xlsx';
            link.click();
            loading.hide();
            showToast('导出成功！', 'success');
        },
        error: function() {
            loading.hide();
            showToast('导出失败，请重试！', 'error');
        }
    });
}

/**
 * 显示加载动画
 * @param {string} message 加载提示
 */
function showLoading(message) {
    var loadingHtml = `
        <div class="loading-overlay">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p>${message}</p>
        </div>
    `;

    var overlay = $(loadingHtml).appendTo('body');

    return {
        hide: function() {
            overlay.fadeOut('fast', function() {
                $(this).remove();
            });
        }
    };
}

/**
 * 表格搜索功能
 */
function initTableSearch() {
    $('#searchInput').on('keyup', function() {
        var value = $(this).val().toLowerCase();
        $('table tbody tr').filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });
}

/**
 * 复制到剪贴板
 * @param {string} text 要复制的文本
 */
function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(function() {
        showToast('已复制到剪贴板！', 'success');
    }, function() {
        // 降级方案
        var textArea = document.createElement("textarea");
        textArea.value = text;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand("copy");
        document.body.removeChild(textArea);
        showToast('已复制到剪贴板！', 'success');
    });
}

// 全局函数导出
window.app = {
    showToast: showToast,
    confirmDelete: confirmDelete,
    exportData: exportData,
    loadStatistics: loadStatistics,
    copyToClipboard: copyToClipboard
};